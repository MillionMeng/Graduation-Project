package com.arvin.service;

import com.arvin.common.Response;
import com.arvin.pojo.Address;
import com.github.pagehelper.PageInfo;

/**
 * create by Arvin Meng
 * Date: 2019/4/21.
 */
public interface IAddressService {

    public Response add(Integer userId, Address address);

    public Response<String> del(Integer userId,Integer addressId);

    public Response<Address> select(Integer userId, Integer addressId);

    public Response update(Integer userId, Address address);

    public Response<PageInfo> list(Integer userId, int pageNum, int pageSize);

}
