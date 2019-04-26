package com.arvin.service;

import com.arvin.common.Response;

import java.util.Map;

/**
 * create by Arvin Meng
 * Date: 2019/4/26.
 */
public interface IOrderService {

    public Response pay(Long orderNo, Integer userId, String path);

    public Response aliCallback(Map<String,String> map);

    public Response queryOrderPayStatus(Integer userId,Long orderNo);

}
