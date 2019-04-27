package com.arvin.service.Impl;

import com.arvin.common.Const;
import com.arvin.common.Response;
import com.arvin.common.TokenCache;
import com.arvin.dao.UserMapper;
import com.arvin.pojo.User;
import com.arvin.service.IUserService;
import com.arvin.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service("iUserService")
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;

    public Response<User> login(String username,String password){

        User user = userMapper.selectByUsername(username);
        if(user == null){
            return Response.createByErrorMessage("用户名不存在");
        }
        password = MD5Util.MD5EncodeUtf8(password);

        User user1 = userMapper.selectByUsernamePassword(username,password);
        if(user1 == null){
            return Response.createByErrorMessage("密码错误");
        }
        return Response.createBySuccess("登陆成功",user1);
    }


    public Response<String> register(User user){

        /*String username = user.getUsername();
        int result = userMapper.checkUsername(username);
        if(result > 0){
            return Response.createByErrorMessage("用户名已存在");
        }

        int result1 = userMapper.checkEmail(user.getEmail());
        if(result1 > 0){
            return Response.createByErrorMessage("邮箱已注册");
        }*/
        Response validResponse = this.checkValid(user.getUsername(),Const.USERNAME);
        if(!validResponse.isSuccess()){
            return validResponse;
        }
        validResponse = this.checkValid(user.getEmail(),Const.EMAIL);
        if(!validResponse.isSuccess()){
            return validResponse;
        }
        user.setRole(Const.Role.ROLE_CUSTOMER);

        String password = user.getPassword();
        user.setPassword(MD5Util.MD5EncodeUtf8(password));

        int resultRow = userMapper.register(user);
        if(resultRow < 0){
            return Response.createByErrorMessage("注册失败");
        }
        return Response.createBySuccessMessage("注册成功");
    }


    public Response<String> checkValid(String str,String type){
        if(org.apache.commons.lang3.StringUtils.isNotBlank(type)){
            //校验
            if(Const.USERNAME.equals(type)){
                int result = userMapper.checkUsername(str);
                if(result > 0){
                    return Response.createByErrorMessage("用户名已存在");
                }
            }
            if(Const.EMAIL.equals(type)){
                int result1 = userMapper.checkEmail(str);
                if(result1 > 0){
                    return Response.createByErrorMessage("邮箱已注册");
                }
            }
        }else {
            return Response.createByErrorMessage("参数错误");
        }
        return Response.createBySuccessMessage("校验成功");
    }


    public Response<String> selectQuestion(String username){
        Response validResponse = this.checkValid(username,Const.USERNAME);
        if(validResponse.isSuccess()){
            //说明用户不存在
            return Response.createByErrorMessage("用户不存在");
        }
        String question = userMapper.selectQuestionByUsername(username);
        if(org.apache.commons.lang3.StringUtils.isNotBlank(question)){
            return Response.createBySuccess(question);
        }
        return Response.createByErrorMessage("密码问题为空");
    }

    public Response<String> checkAnswer(String username,String question,String answer){
        int resultCount = userMapper.checkAnswer(username,question,answer);
        if(resultCount > 0){
            String forgetToken = UUID.randomUUID().toString();
            TokenCache.setKey(TokenCache.TOKEN_PREFIX+username,forgetToken);
            return Response.createBySuccess(forgetToken);
        }
        return Response.createByErrorMessage("答案错误");
    }


    public Response<String> forgetResetPassword(String username,String passwordNew,String forgetToken) {
        if (org.apache.commons.lang3.StringUtils.isBlank(forgetToken)) {
            return Response.createByErrorMessage("参数错误,token需要传递");
        }
        Response validResponse = this.checkValid(username, Const.USERNAME);
        if (validResponse.isSuccess()) {
            //用户不存在
            return Response.createByErrorMessage("用户不存在");
        }
        String token = TokenCache.getKey(TokenCache.TOKEN_PREFIX+username);
        if(org.apache.commons.lang3.StringUtils.isBlank(token)){
            return Response.createByErrorMessage("token无效或者过期");
        }
        if(org.apache.commons.lang3.StringUtils.equals(forgetToken,token)) {
            String md5Password = MD5Util.MD5EncodeUtf8(passwordNew);
            int rowCount= userMapper.updatePasswordByUsername(username,md5Password);

            if(rowCount > 0){
                return  Response.createBySuccessMessage("修改密码成功");
            }else {
                return Response.createByErrorMessage("token错误，请重新获取重置密码的token");
            }
        }
        return Response.createByErrorMessage("修改密码失败");
    }

    public Response<String> resetPassword(String passwordOld,String passwordNew,User user) {
        //防止横向越权，要校验一下这个用户的旧密码，一定要指定是这个用户
        //因为我们会查询一个count(1),如果不指定id，那么结果就是true :count>0;
        int resultCount = userMapper.checkPassword(MD5Util.MD5EncodeUtf8(passwordOld), user.getId());
        if (resultCount == 0) {
            return Response.createByErrorMessage("原密码输入错误");
        }
        user.setPassword(MD5Util.MD5EncodeUtf8(passwordNew));
        int updateCount = userMapper.updateByPrimaryKeySelective(user);
        if (updateCount > 0) {
            return Response.createBySuccessMessage("密码更新成功");
        }
        return Response.createByErrorMessage("密码更新失败");
    }

    public Response<User> updateInformation(User user){
        //username不能被更新
        //email也要进行一个校验，校验新的email是不是已经存在，并且存在的email如果相同的话，不能是我们当前的这个用户.
        int resultCount = userMapper.checkemailByUserId(user.getEmail(),user.getId());
        if(resultCount > 0){
            return Response.createByErrorMessage("email已存在，请更新email再尝试更新");
        }
        User updateUser = new User();
        updateUser.setUsername(user.getUsername());
        updateUser.setId(user.getId());
        updateUser.setEmail(user.getEmail());
        updateUser.setPhone(user.getPhone());
        updateUser.setQuestion(user.getQuestion());
        updateUser.setAnswer((user.getAnswer()));
        int updateCount = userMapper.updateByPrimaryKeySelective(updateUser);
        if(updateCount > 0){
            return Response.createBySuccess("更新个人信息成功",updateUser);
        }
        return Response.createByErrorMessage("更新个人信息失败");
    }

    public Response<User> getInformation(Integer userId){
        User user = userMapper.selectByPrimaryKey(userId);
        if(user == null){
            return Response.createByErrorMessage("找不到当前用户");
        }
        user.setPassword(org.apache.commons.lang3.StringUtils.EMPTY);
        return Response.createBySuccess(user);
    }

    //后台

    /**
     * 校验是否是管理员
     * @param user
     * @return
     */
    public Response checkAdmin(User user){
        if(user != null && user.getRole().intValue() ==Const.Role.ROLE_ADMIN){
            return Response.createBySuccess();
        }
        return Response.createByError();
    }
}
