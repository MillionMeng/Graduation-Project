package com.arvin.controller.backend;

import com.arvin.common.Const;
import com.arvin.common.Response;
import com.arvin.common.ResponseCode;
import com.arvin.pojo.User;
import com.arvin.service.ICategoryService;
import com.arvin.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

/**
 * create by Arvin Meng
 * Date: 2019/4/16.
 */
@Controller
@CrossOrigin(origins = {"http://localhost:8086", "http://localhost:8088"})
public class CategoryManageController {

    @Autowired
    private IUserService iUserService;
    @Autowired
    private ICategoryService iCategoryService;

    /**
     * 增加分类
     * @param session
     * @param categoryName
     * @param parentId
     * @return
     */

    @RequestMapping(value = "/category/add", method = RequestMethod.POST)
    @ResponseBody
    public Response addCategory(HttpSession session, @RequestParam String categoryName, @RequestParam(value = "parentId", defaultValue = "0") int parentId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return Response.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请登录");
        }
        //校验是否是管理员
        if (iUserService.checkAdmin(user).isSuccess()) {
            //增加处理分类的逻辑
            return iCategoryService.addCategory(categoryName, parentId);

        } else {
            return Response.createByErrorMessage("无权限，需要管理员");
        }
    }

    /**
     * 更新分类名称
     * @param session
     * @param categoryId
     * @param categoryName
     * @return
     */
    @RequestMapping(value = "/category/setname", method = RequestMethod.POST)
    @ResponseBody
    public Response setCategoryName(HttpSession session, @RequestParam Integer categoryId,@RequestParam String categoryName) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        /*if (user == null) {
            return Response.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请登录");
        }*/
        if (iUserService.checkAdmin(user).isSuccess()) {
            //更新categoryName(分类名称)
            return iCategoryService.updateCategoryName(categoryId,categoryName);
        }else{
            return Response.createByErrorMessage("无权限，需要管理员");
        }
    }

    /**
     * 查找分类节点 （平级）
     * @param session
     * @param categoryId
     * @return
     */
    @RequestMapping("/category/get_category")
    @ResponseBody
    public Response getChildrenParallelCategory(HttpSession session,@RequestParam(value = "categoryId" ,defaultValue = "0") Integer categoryId){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return Response.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录,请登录");
        }
        if(iUserService.checkAdmin(user).isSuccess()){
            //查询子节点的category信息,并且不递归,保持平级
            return iCategoryService.getChildrenParallelCategory(categoryId);
        }else{
            return Response.createByErrorMessage("无权限操作,需要管理员权限");
        }
    }



    /**
     *  目前没用到
     *
     *
     * 递归查找孩子节点
     * @param session
     * @param categoryId
     * @return
     */
    @RequestMapping(value = "/category/get_children_category", method = RequestMethod.GET)
    @ResponseBody
    public Response getCategoryAndDeepChildrenCategory(HttpSession session,@RequestParam(value = "categoryId" ,defaultValue = "0") Integer categoryId){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        /*if(user == null){
            return Response.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录,请登录");
        }*/
        if(iUserService.checkAdmin(user).isSuccess()){
            //查询当前节点的id和递归子节点的id
//            0->10000->100000
            return iCategoryService.selectCategoryAndChildrenById(categoryId);

        }else{
            return Response.createByErrorMessage("无权限操作,需要管理员权限");
        }
    }


}
