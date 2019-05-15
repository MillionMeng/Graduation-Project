package com.arvin.service;

import com.arvin.common.Response;
import com.arvin.vo.CartVo;

/**
 * create by Arvin Meng
 * Date: 2019/4/19.
 */
public interface ICartService {
    public Response<CartVo> add(Integer userId, Integer productId, Integer count,String remarks);

    public Response<CartVo> update(Integer userId,Integer productId,Integer count);

    public Response<CartVo> deleteProduct(Integer userId,String productIds);

    public Response<CartVo> list(Integer userId);

    public Response<CartVo> selectOrUnSelect(Integer userId,Integer checked,Integer productId);

    public Response<Integer> getCartProductCount(Integer userId);
}
