package com.arvin.service;

import com.arvin.common.Response;
import com.arvin.pojo.Comment;

/**
 * create by Arvin Meng
 * Date: 2019/5/9.
 */
public interface ICommentService {

    public Response addComment(Comment comment);

    public Response commentList(Integer productId);

    public Response Permission(Comment comment);

}
