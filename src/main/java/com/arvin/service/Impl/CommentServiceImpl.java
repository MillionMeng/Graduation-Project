package com.arvin.service.Impl;

import com.arvin.common.Response;
import com.arvin.common.ResponseCode;
import com.arvin.dao.CommentMapper;
import com.arvin.dao.Order_itemMapper;
import com.arvin.pojo.Comment;
import com.arvin.service.ICommentService;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * create by Arvin Meng
 * Date: 2019/5/9.
 */
@Service("iCommentService")
public class CommentServiceImpl implements ICommentService {

    private Logger logger = LoggerFactory.getLogger(CommentServiceImpl.class);

    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private Order_itemMapper orderItemMapper;

    public Response addComment(Comment comment){
        if(comment.getProductId() == null){
            return Response.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        //去订单明细表查看 该用户是否购买过该商品，如果没有，则不能评论

        int resRow = orderItemMapper.SelectByUserIdAndProductId(comment.getProductId(),comment.getUserId());
        if(resRow == 0){
            Response.createByErrorMessage("您未购买该商品，无法评论！");
        }
        int row = commentMapper.insert(comment);
        if(row > 0){
            return Response.createBySuccess(comment);
        }
        return Response.createByErrorMessage("新增评论出了问题~");

    }


    public Response commentList(Integer productId){
        //该商品是否有评论
        int row = commentMapper.selectCommentCount(productId);
        if(row == 0){
            Map<String,String> map  = new HashMap<>();
            map.put("Msg","暂无评价~");
            return Response.createBySuccess(map);
        }

        //查询评论列表
        List<Map<String,String>> list = commentMapper.selectList(productId);

        Map<String,Object> map = new HashMap<>();



        map.put("res",list);
        return Response.createBySuccess(map);
    }


    public Response Permission(Comment comment){
        int resRow = orderItemMapper.SelectByUserIdAndProductId(comment.getProductId(),comment.getUserId());
        logger.error("返回结果"+resRow,resRow);
        if(resRow == 0){
            return Response.createByErrorMessage("您未购买该商品，无法评论！");
        }
        return Response.createBySuccess();
    }

    @Test
    public void test(){
        try {
            List<Map<String, String>> list = commentMapper.selectList(1);
            for(Map<String,String> list1 : list){
                for(String k : list1.keySet()){
                    System.out.println(k + ":" + list1.get(k));
                }
            }
        }catch (RuntimeException e){
            logger.error("查询异常" ,e);
        }

    }



}
