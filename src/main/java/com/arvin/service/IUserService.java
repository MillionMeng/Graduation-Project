package com.arvin.service;

import com.arvin.common.Response;
import com.arvin.pojo.User;

public interface IUserService {

    public Response<User> login(String username, String password);

    public Response<String> register(User user);

    public Response<String> checkValid(String str,String type);

    public Response<String> selectQuestion(String username);

    public Response<String> checkAnswer(String username,String question,String answer);

    public Response<String> forgetResetPassword(String username,String passwordNew,String forgetToken) ;

    public Response<String> resetPassword(String passwordOld,String passwordNew,User user);

    public Response<User> updateInformation(User user);

    public Response<User> getInformation(Integer userId);

    public Response checkAdmin(User user);

    public Response getStatistic();

    public Response getUserList(int pageNum,int pageSize);

}
