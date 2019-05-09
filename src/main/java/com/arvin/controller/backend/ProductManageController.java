package com.arvin.controller.backend;

import com.arvin.common.Const;
import com.arvin.common.Response;
import com.arvin.common.ResponseCode;
import com.arvin.pojo.Product;
import com.arvin.pojo.User;
import com.arvin.service.IFileService;
import com.arvin.service.IProductService;
import com.arvin.service.IUserService;
import com.arvin.util.PropertiesUtil;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * create by Arvin Meng
 * Date: 2019/4/17.
 */
@Controller
@CrossOrigin(origins = {"http://localhost:8086", "http://localhost:8088"})
public class ProductManageController {

    @Autowired
    private IUserService iUserService;
    @Autowired
    private IProductService iProductService;
    @Autowired
    private IFileService iFileService;

    /**
     * 更新或添加商品
     * @param session
     * @param product
     * @return
     */
    @RequestMapping(value = "/product/save",method = RequestMethod.POST)
    @ResponseBody
    public Response productSaveOrUpdate(HttpSession session,Product product){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return Response.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录");
        }
        if(iUserService.checkAdmin(user).isSuccess()){
            return iProductService.saveOrUpdateProduct(product);
        }else{
            return Response.createByErrorMessage("无权限操作");
        }
    }

    /**
     * 更改产品销售状态
     * @param session
     * @param productId
     * @param status
     * @return
     */
    @RequestMapping(value = "/product/setstatus",method = RequestMethod.POST)
    @ResponseBody
    public Response setSaleStatus(HttpSession session, Integer productId,Integer status){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return Response.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录");
        }
        if(iUserService.checkAdmin(user).isSuccess()){
            return iProductService.setSaleStatus(productId,status);
        }else{
            return Response.createByErrorMessage("无权限操作");
        }
    }

    /**
     * 获取商品详情
     * @param session
     * @param productId
     * @return
     */
    @RequestMapping(value = "/product/detail",method = RequestMethod.GET)
    @ResponseBody
    public Response getDetail(HttpSession session, Integer productId){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return Response.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录");
        }
        if(iUserService.checkAdmin(user).isSuccess()){
            return iProductService.manageProductDetail(productId);
        }else{
            return Response.createByErrorMessage("无权限操作");
        }
    }

    /**
     * 商品列表
     * @param session
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/product/list",method = RequestMethod.GET)
    @ResponseBody
    public Response getList(HttpSession session, @RequestParam(value = "pageNum",defaultValue = "1") int pageNum, @RequestParam(value = "pageSize",defaultValue = "10") int pageSize){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return Response.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录");
        }
        if(iUserService.checkAdmin(user).isSuccess()){
            return iProductService.getProductList(pageNum,pageSize);
        }else{
            return Response.createByErrorMessage("无权限操作");
        }
    }

    /**
     * 搜索商品
     * @param session
     * @param productName
     * @param productId
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/product/search",method = RequestMethod.GET)
    @ResponseBody
    public Response productSearch(HttpSession session,String productName,Integer productId, @RequestParam(value = "pageNum",defaultValue = "1") int pageNum,@RequestParam(value = "pageSize",defaultValue = "10") int pageSize){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return Response.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录");
        }
        if(iUserService.checkAdmin(user).isSuccess()){
            return iProductService.searchProduct(productName,productId,pageNum,pageSize);
        }else{
            return Response.createByErrorMessage("无权限操作");
        }
    }


    /**
     * 上传图片到ftp服务器
     * @param session
     * @param file
     * @param request
     * @return
     */
    @RequestMapping("/product/upload")
    @ResponseBody
    public Response upload(HttpSession session, @RequestParam(value = "upload_file",required = false) MultipartFile file, HttpServletRequest request) {
        /*User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return Response.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录,请登录管理员");
        }*/
        //if (iUserService.checkAdmin(user).isSuccess()) {
        String path = request.getSession().getServletContext().getRealPath("upload");
        String targetFileName = iFileService.upload(file, path);
        String url = PropertiesUtil.getProperty("ftp.server.http.prefix") + targetFileName;

        Map fileMap = Maps.newHashMap();
        fileMap.put("uri", targetFileName);
        fileMap.put("url", url);
        return Response.createBySuccess(fileMap);
    /*} else {
            return Response.createByErrorMessage("无权限操作");
        }*/
    }



    @RequestMapping("/product/richtext_img_upload")
    @ResponseBody
    public Map richtextImgUpload(HttpSession session, @RequestParam(value = "upload_file",required = false) MultipartFile file, HttpServletRequest request, HttpServletResponse response) {
        Map resultMap = Maps.newHashMap();
       /* User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            resultMap.put("success", false);
            resultMap.put("msg", "请登录");
            return resultMap;
        }*/
        //按照simditor的要求进行返回

        //if(iUserService.checkAdmin(user).isSuccess()){
            String path = request.getSession().getServletContext().getRealPath("upload");
            String targetFileName = iFileService.upload(file,path);
            if(StringUtils.isBlank(targetFileName)){
                resultMap.put("success",false);
                resultMap.put("msg","上传失败");
                return resultMap;
            }
            String url = PropertiesUtil.getProperty("ftp.server.http.prefix")+targetFileName;
            resultMap.put("success",true);
            resultMap.put("msg","上传成功");
            resultMap.put("file_path",url);
            response.addHeader("Access-Control-Allow-Headers","X-File-Name");
            return resultMap;
       /* }else{
            resultMap.put("success",false);
            resultMap.put("msg","无权限操作");
            return resultMap;
        }*/
    }




    public static void main(String[] args) {
        String str="a,b,c,d,e,f,g,h,i,j,k,l,m,n,o,p,q,r,s,t,u,v,w,x,y,z";
        String[] strArray = null;
        strArray = str.split(",");
        for(String s : strArray){
            System.out.println(s);
        }
    }
}


