package com.arvin.dao;

import com.arvin.pojo.Collect;

import java.util.List;
import java.util.Map;

/**
 * create by Arvin Meng
 * Date: 2019/5/10.
 */
public interface CollectMapper {

    int selectCollect(Collect collect);

    int insertCollect(Collect collect);

    List<Map<String,String>> selectCollectList(Integer userId);

    int deleteCollect(Collect collect);

}
