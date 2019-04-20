package com.arvin.controller.Client;

import com.arvin.common.Const;
import com.arvin.common.Response;
import com.arvin.common.ResponseCode;
import com.arvin.pojo.User;
import com.arvin.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

//CrossOrigin(origins = {"http://localhost:8088", "null"})
@Controller
public class UserController {
    @Autowired
    private IUserService IUserService;

    /**
     * 登陆接口
     */
    @CrossOrigin(origins = {"http://localhost:8088", "null"})
    @RequestMapping(value = "/user/login", method = RequestMethod.POST)
    @ResponseBody
    public Response<User> login(@RequestParam String username,@RequestParam String password,HttpSession session){

        Response<User> response = IUserService.login(username,password);
        if(response.isSuccess()){
            session.setAttribute(Const.CURRENT_USER,response.getData());
        }
        return response;
    }

    /**
     * 退出登录
     * @param session
     * @return
     */
    @RequestMapping(value = "/user/logout", method = RequestMethod.GET)
    @ResponseBody
    public Response<String> logout(HttpSession session){
        session.removeAttribute(Const.CURRENT_USER);
        return Response.createBySuccess();
    }

    /**
     * 注册
     * @param user
     * @return
     */

    @CrossOrigin(origins = {"http://localhost:8088", "null"})
    @RequestMapping(value = "/user/register",method = RequestMethod.POST)
    @ResponseBody
    public Response<String> register(@RequestBody User user){
        return IUserService.register(user);
    }

    /**
     * 检查用户名 邮箱
     * @param str
     * @param type
     * @return
     */
    @CrossOrigin(origins = {"http://localhost:8088", "null"})
    @RequestMapping(value = "/user/check",method = RequestMethod.POST)
    @ResponseBody
    public Response<String> checkVaild(@RequestParam String str,@RequestParam String type){
        return IUserService.checkValid(str,type);
    }

    /**
     * 获取用户信息
     * @param session
     * @return
     */
    @RequestMapping(value = "/user/loginfo", method = RequestMethod.GET)
    @ResponseBody
    public Response<User> getUserInfo(HttpSession session){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user != null){
            return Response.createBySuccess(user);
        }

        return Response.createByErrorMessage("用户未登录，无法获取当前用户的信息");
    }



    /**
     * 获取个人信息
     * @param session
     * @return
     */
    @RequestMapping(value = "/user/getInformation", method = RequestMethod.POST)
    @ResponseBody
    public Response<User> getInformation(HttpSession session){
        User currentUser = (User)session.getAttribute(Const.CURRENT_USER);
        if(currentUser == null){
            return Response.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"未登录,需要强制登录status=10");
        }
        return IUserService.getInformation(currentUser.getId());
    }

    /**
     * 更新个人信息
     * @param session
     * @param user
     * @return
     */
    @RequestMapping(value = "/user/updateInfomation", method = RequestMethod.POST)
    @ResponseBody
    public Response<User> updateInfomation(HttpSession session,User user){
        User currentuser = (User)session.getAttribute(Const.CURRENT_USER);
        if(currentuser == null){
            return Response.createByErrorMessage("用户未登录");
        }
        user.setId(currentuser.getId());
        user.setUsername(currentuser.getUsername());
        Response<User> response = IUserService.updateInformation(user);
        if(response.isSuccess()){
            session.setAttribute(Const.CURRENT_USER,response.getData());
        }
        return response;
    }

    /**
     * 密保问题
     * @param username
     * @return
     */
    @RequestMapping(value = "/user/forgetQuestion", method = RequestMethod.GET)
    @ResponseBody
    public Response<String> forgetQuestion(@RequestParam String username){
        return IUserService.selectQuestion(username);
    }

    /**
     * 校验答案
     * @param username
     * @return
     */
    @RequestMapping(value = "/user/checkanswer", method = RequestMethod.POST)
    @ResponseBody
    public Response<String> checkAnswer(@RequestParam String username,@RequestParam String question,@RequestParam String answer){
        return IUserService.checkAnswer(username,question,answer);

    }

    /**
     *忘记密码
     * @param username
     * @param passwordNew
     * @param forgetToken
     * @return
     */
    @RequestMapping(value = "/user/resetpassword", method = RequestMethod.POST)
    @ResponseBody
    public Response<String> forgetResetPassword(String username,String passwordNew,String forgetToken){
        return IUserService.forgetResetPassword(username,passwordNew,forgetToken);
    }

    /**
     * 登陆状态下的重置密码
     * @param session
     * @param password
     * @param passwordNew
     * @return
     */
    @RequestMapping(value = "/user/resetpasswordlogin", method = RequestMethod.POST)
    @ResponseBody
    public Response resetPassword(HttpSession session,String password,String passwordNew){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return Response.createByErrorMessage("用户未登录");
        }

        return IUserService.resetPassword(password,passwordNew,user);
    }







}
