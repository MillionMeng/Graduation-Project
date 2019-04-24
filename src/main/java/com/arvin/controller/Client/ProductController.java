package com.arvin.controller.Client;


import com.arvin.common.Response;
import com.arvin.service.IProductService;
import com.arvin.vo.ProductDetailVo;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@CrossOrigin(origins = {"http://localhost:8088", "null"})
public class ProductController {

    @Autowired
    private IProductService iProductService;

    /**
     * 客户端获取商品详情
     * @param productId
     * @return
     */
    @RequestMapping("/productfe/detail")
    @ResponseBody
    public Response<ProductDetailVo> detall(Integer productId){
        return iProductService.getProductDetail(productId);
    }


    /**
     * 客户端商品list
     * @param keyword
     * @param categoryId
     * @param pageNum
     * @param pageSize
     * @param orderBy
     * @return
     */
    @CrossOrigin(origins = {"http://localhost:8088", "null"})
    @RequestMapping(value="/productfe/list" ,method = RequestMethod.GET)
    @ResponseBody
    public Response<PageInfo> list(@RequestParam(value = "keyword",required = false)String keyword,
                                   @RequestParam(value = "categoryId",required = false)Integer categoryId,
                                   @RequestParam(value ="pageNum",defaultValue = "1")int pageNum,
                                   @RequestParam(value ="pageSize",defaultValue = "10")int pageSize,
                                   @RequestParam(value ="orderBy",defaultValue = "")String orderBy){

        return iProductService.getProductByKeywordCategory(keyword,categoryId,pageNum,pageSize,orderBy);
    }
}
