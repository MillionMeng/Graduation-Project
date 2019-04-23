package com.arvin.service;

import com.arvin.common.Response;
import com.arvin.pojo.Product;
import com.arvin.vo.ProductDetailVo;
import com.github.pagehelper.PageInfo;

/**
 * create by Arvin Meng
 * Date: 2019/4/17.
 */
public interface IProductService {

    public Response saveOrUpdateProduct(Product product);

    public Response<String> setSaleStatus(Integer productId, Integer status);

    public Response<ProductDetailVo> manageProductDetail(Integer productId);

    public Response<PageInfo> getProductList(int pageNum, int pageSize);

    public Response<PageInfo> searchProduct(String productName, Integer productId, int pageNum, int pageSize);

}
