package com.arvin.service.Impl;

import com.arvin.common.Response;
import com.arvin.common.ResponseCode;
import com.arvin.dao.CategoryMapper;
import com.arvin.dao.ProductMapper;
import com.arvin.pojo.Category;
import com.arvin.pojo.Product;
import com.arvin.service.IProductService;
import com.arvin.util.DateTimeUtil;
import com.arvin.util.PropertiesUtil;
import com.arvin.vo.ProductDetailVo;
import com.arvin.vo.ProductListVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * create by Arvin Meng
 * Date: 2019/4/17.
 */
@Service("iProductService")
public class ProductServiceImpl implements IProductService {

    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private CategoryMapper categoryMapper;


    public Response saveOrUpdateProduct(Product product) {
        if (product != null) {
            if (StringUtils.isNotBlank(product.getSubImages())) {//判断子图是否为空 若不为空 获取第一个子图为主图
                String[] subImageArray = product.getSubImages().split(",");
                if (subImageArray.length > 0) {
                    product.setMainImage(subImageArray[0]);
                }
            }
            if (product.getId() != null) {
                int rowCount = productMapper.updateByPrimaryKey(product);
                if (rowCount > 0) {
                    return Response.createBySuccess("跟新商品品成功");
                } else {
                    return Response.createByErrorMessage("更新商品失败");
                }
            } else {
                int rowCount = productMapper.insert(product);
                if (rowCount > 0) {
                    return Response.createBySuccess("新增商品成功");
                } else {
                    return Response.createByErrorMessage("更新商品失败");
                }

            }
        }
        return Response.createByErrorMessage("新增或更新商品参数不正确");
    }

    public Response<String> setSaleStatus(Integer productId, Integer status) {
        if(productId == null || status == null){
            return Response.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Product product= new Product();
        product.setId(productId);
        product.setStatus(status);
        int rowCount = productMapper.updateByPrimaryKeySelective(product);
        if(rowCount > 0){
            return Response.createBySuccess("修改商品销售状态成功");
        }
        return  Response.createByErrorMessage("修改商品品销售状态失败");
    }


    public Response<ProductDetailVo> manageProductDetail(Integer productId) {
        if(productId == null){
            return Response.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Product product = productMapper.selectByPrimaryKey(productId);
        if(product == null){
            return Response.createByErrorMessage("产品已下架或者删除");
        }
        //vo对象 --value object    pojo--bo(business object)--vo(view object)
        ProductDetailVo productDetailVo = ProductConversionVo(product);
        return Response.createBySuccess(productDetailVo);
    }


    private ProductDetailVo ProductConversionVo(Product product){
        ProductDetailVo productDetailVo = new ProductDetailVo();
        productDetailVo.setId(product.getId());
        productDetailVo.setSubtitle(product.getSubtitle());
        productDetailVo.setPrice(product.getPrice());
        productDetailVo.setMainImage(product.getMainImage());
        productDetailVo.setSubImages(product.getSubImages());
        productDetailVo.setCategoryId(product.getCategoryId());
        productDetailVo.setDetail(product.getDetail());
        productDetailVo.setName(product.getName());
        productDetailVo.setStatus(product.getStatus());
        productDetailVo.setStock(product.getStock());
        productDetailVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix","http://img.happymmall.com/"));

        Category category = categoryMapper.selectByPrimaryKey(product.getCategoryId());
        if(category == null){
            productDetailVo.setParentCategoryId(0);//默认根节点
        }else{
            productDetailVo.setParentCategoryId(category.getParentId());
        }

        productDetailVo.setCreateTime(DateTimeUtil.dateToStr(product.getCreateTime()));
        productDetailVo.setUpdateTime(DateTimeUtil.dateToStr(product.getUpdateTime()));
        return productDetailVo;
    }


    public Response<PageInfo> getProductList(int pageNum, int pageSize) {
        //startPage -start
        PageHelper.startPage(pageNum,pageSize);
        List<Product> productList = productMapper.selectList();
        List<ProductListVo> productListVoList = Lists.newArrayList();

        //填充自己的sql查询逻辑
        for(Product productItem : productList){
            ProductListVo productListVo = ProducListtConversionVo(productItem);
            productListVoList.add(productListVo);
        }
        //pageHelper-收尾

        //todo 直接PageInfo pageresult = new PageInfo(productListVoList); 试试
        PageInfo pageresult = new PageInfo(productList);
        pageresult.setList(productListVoList);

        return Response.createBySuccess(pageresult);
    }

    private ProductListVo ProducListtConversionVo(Product product){
        ProductListVo productListVo = new ProductListVo();
        productListVo.setId(product.getId());
        productListVo.setName(product.getName());
        productListVo.setCategoryId(product.getCategoryId());
        productListVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix","http://img.happymmall.com/"));
        productListVo.setMainImage(product.getMainImage());
        productListVo.setPrice(product.getPrice());
        productListVo.setSubtitle(product.getSubtitle());
        productListVo.setStatus(product.getStatus());
        return productListVo;
    }

    public Response<PageInfo> searchProduct(String productName, Integer productId, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        if(StringUtils.isNotBlank(productName)){
            productName = new StringBuilder().append("%").append(productName).append("%").toString();
        }
        List<Product> productList = productMapper.selectByNameAndProductId(productName,productId);
        List<ProductListVo> productListVoList = Lists.newArrayList();

        for(Product productItem : productList){
            ProductListVo productListVo = ProducListtConversionVo(productItem);
            productListVoList.add(productListVo);
        }
        PageInfo pageresult = new PageInfo(productList);
        pageresult.setList(productListVoList);

        return Response.createBySuccess(pageresult);
    }
}
