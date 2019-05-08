package com.arvin.controller.backend;

import com.arvin.common.Const;
import com.arvin.common.Response;
import com.arvin.common.ResponseCode;
import com.arvin.pojo.User;
import com.arvin.service.IOrderService;
import com.arvin.service.IProductService;
import com.arvin.service.IUserService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

/**
 * create by Arvin Meng
 * Date: 2019/4/15.
 */
@Controller
@CrossOrigin(origins = {"http://localhost:8086", "null"})
public class UserManagerController {

    @Autowired
    private IUserService iUserService;
    @Autowired
    private IProductService iProductService;
    @Autowired
    private IOrderService iOrderService;



    /**
     * 管理员登陆
     * */
    @RequestMapping(value = "/manage/user/login", method = RequestMethod.POST)
    @ResponseBody
    public Response<User> login(@Param("username") String username, @Param("password") String password, HttpSession session) {
        Response<User> response = iUserService.login(username, password);
        if (response.isSuccess()) {
            User user = response.getData();
            if (user.getRole() == Const.Role.ROLE_ADMIN) {
                //说明登陆的是管理员
                session.setAttribute(Const.CURRENT_USER, response.getData());
                return response;
            } else {
                return Response.createByErrorMessage("不是管理员，无法登陆");
            }
        }
        return response;
    }



    /**
     * 后台获取 用户，商品，订单数量
     * @param session
     * @return
     */
    @RequestMapping(value = "/manage/get/statistic", method = RequestMethod.GET)
    @ResponseBody
    public Response getStatisticCount(HttpSession session){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return Response.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"请登录");

        }
        if(iUserService.checkAdmin(user).isSuccess()){
            return iUserService.getStatistic();
        }else{
            return Response.createByErrorMessage("您没有权限");
        }
    }

    /**
     * 后台获取用户列表
     * @param session
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/manage/get_user_list", method = RequestMethod.GET)
    @ResponseBody
    public Response getUserList(HttpSession session, @RequestParam(value = "pageNum",defaultValue = "1") int pageNum,
                                @RequestParam(value = "pageSize",defaultValue = "10")int pageSize){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return Response.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"请登录");

        }
        return iUserService.getUserList(pageNum,pageSize);
    }

}