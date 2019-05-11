package com.arvin.controller.Client;

import com.arvin.common.Const;
import com.arvin.common.Response;
import com.arvin.common.ResponseCode;
import com.arvin.pojo.Comment;
import com.arvin.pojo.User;
import com.arvin.service.ICommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

/**
 * create by Arvin Meng
 * Date: 2019/5/9.
 */
@CrossOrigin(origins = {"http://localhost:8086", "http://localhost:8088"})
@Controller
public class CommentController {

    @Autowired
    private ICommentService iCommentService;


    /**
     * 编写评论
     * @param session
     * @param comment
     * @return
     */
    @RequestMapping(value = "/comment/add" ,method = RequestMethod.POST)
    @ResponseBody
    public Response addComment(HttpSession session, @RequestBody Comment comment){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return Response.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        comment.setUserId(user.getId());
        return iCommentService.addComment(comment);
    }


    /**
     * 评论列表
     * @param session
     * @param productId
     * @return
     */
    @RequestMapping(value = "/comment/list" ,method = RequestMethod.GET)
    @ResponseBody
    public Response commentList(HttpSession session,Integer productId){
        /*User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return Response.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }*/
        return iCommentService.commentList(productId);

    }


    /**
     * 判断用户是否有评论权限
     * @param session
     * @param comment
     * @return
     */
    @RequestMapping(value = "/comment/Permission" ,method = RequestMethod.GET)
    @ResponseBody
    public Response commentPermission(HttpSession session,Comment comment){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return Response.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        comment.setUserId(user.getId());
        return iCommentService.Permission(comment);

    }

}


