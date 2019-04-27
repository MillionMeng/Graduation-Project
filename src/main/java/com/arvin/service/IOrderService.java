package com.arvin.service;

import com.arvin.common.Response;
import com.arvin.vo.OrderVo;
import com.github.pagehelper.PageInfo;

import java.util.Map;

/**
 * create by Arvin Meng
 * Date: 2019/4/26.
 */
public interface IOrderService {

    public Response pay(Long orderNo, Integer userId, String path);

    public Response aliCallback(Map<String,String> map);

    public Response queryOrderPayStatus(Integer userId,Long orderNo);

    public Response createOrder(Integer userId,Integer shippingId);

    public Response<String> cancel(Integer userId,Long orderNo);

    public Response getOrderCartProduct(Integer userId);

    public Response<OrderVo> getOrderDetail(Integer userId, Long orderNo);

    public Response<PageInfo> getOrderList(Integer userId, int pageNum, int pageSize);

    public Response<PageInfo> manageList(int pageNum,int pageSize);

    public Response<OrderVo> manageDetail(Long orderNo);

    public Response<PageInfo> manageSearch(Long orderNo,int pageNum,int pageSize);

    public Response<String> manageSendGoods(Long orderNo);

}
