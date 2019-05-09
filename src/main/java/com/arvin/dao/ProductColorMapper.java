package com.arvin.dao;

import org.apache.ibatis.annotations.Param;

/**
 * create by Arvin Meng
 * Date: 2019/5/9.
 */
public interface ProductColorMapper {

    int insert(@Param("productId")int productId,@Param("colorId") int colorId);
}
