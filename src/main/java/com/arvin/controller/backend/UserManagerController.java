package com.arvin.controller.backend;

import com.arvin.common.Const;
import com.arvin.common.Response;
import com.arvin.pojo.User;
import com.arvin.service.IUserService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * create by Arvin Meng
 * Date: 2019/4/15.
 */
@Controller
public class UserManagerController {

    @Autowired
    private IUserService iUserService;

    /**
     * 管理员登陆
     * */
    @RequestMapping(value = "manage/user/login", method = RequestMethod.POST)
    @ResponseBody
    public Response<User> login(@Param("username") String username, @Param("password") String password, HttpSession session) {
        Response<User> response = iUserService.login(username, password);
        if (response.isSuccess()) {
            User user = response.getData();
            if (user.getRole() == Const.Role.ROLE_CUSTOMER) {
                //说明登陆的是管理员
                session.setAttribute(Const.CURRENT_USER, user);
                return response;
            } else {
                return Response.createByErrorMessage("不是管理员，无法登陆");
            }
        }
        return response;
    }
}