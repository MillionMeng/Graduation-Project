package com.arvin.dao;

import com.arvin.pojo.Comment;

import java.util.List;
import java.util.Map;

/**
 * create by Arvin Meng
 * Date: 2019/5/9.
 */
public interface CommentMapper {

    int insert(Comment comment);

    List<Map<String,String>> selectList(Integer productId);

    int selectCommentCount(Integer productId);

    int selectComment();


}
