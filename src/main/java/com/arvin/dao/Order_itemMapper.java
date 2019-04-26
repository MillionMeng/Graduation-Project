package com.arvin.dao;

import com.arvin.pojo.Order_item;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface Order_itemMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Order_item record);

    int insertSelective(Order_item record);

    Order_item selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Order_item record);

    int updateByPrimaryKey(Order_item record);

    List<Order_item> getByOrderNoUserId(@Param("orderNo")Long orderNo, @Param("userId")Integer userId);

}