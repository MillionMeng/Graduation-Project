package com.arvin.dao;

import com.arvin.pojo.Color;

/**
 * create by Arvin Meng
 * Date: 2019/5/9.
 */
public interface ColorMapper {

    int insert(Color color);

    Color selectByColorName(String name);
}
