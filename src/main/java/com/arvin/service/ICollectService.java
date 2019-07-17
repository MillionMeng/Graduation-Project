package com.arvin.service;

import com.arvin.common.Response;
import com.arvin.pojo.Collect;

/**
 * create by Arvin Meng
 * Date: 2019/5/10.
 */
public interface ICollectService {

    public Response addCollect(Collect collect);

    public Response collectList(Integer userId);

    public Response delCollect(Collect collect);

}
