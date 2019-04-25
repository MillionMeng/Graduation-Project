package com.arvin.service.Impl;

import com.arvin.common.Response;
import com.arvin.dao.AddressMapper;
import com.arvin.pojo.Address;
import com.arvin.service.IAddressService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * create by Arvin Meng
 * Date: 2019/4/25.
 */
@Service("iAddressService")
public class AddressServiceImpl implements IAddressService{

    @Autowired
    private AddressMapper addressMapper;

    public Response add(Integer userId, Address address){
        address.setUserId(userId);
        int rowCount = addressMapper.insert(address);
        if(rowCount > 0){
            Map result = Maps.newHashMap();
            result.put("addressId",address.getId());
            return Response.createBySuccess("新建地址成功",result);
        }
        return Response.createByErrorMessage("新建地址失败");
    }

    public Response<String> del(Integer userId,Integer addressId){
        int resultCount = addressMapper.deleteByAddressIdUserId(userId,addressId);
        if(resultCount > 0){
            return Response.createBySuccess("删除地址成功");
        }else {
            return Response.createByErrorMessage("删除地址失败");
        }
    }

    public Response<Address> select(Integer userId, Integer addressId){
        Address address = addressMapper.selectByAddressIdUserId(userId,addressId);
        if(address == null){
            return  Response.createByErrorMessage("无法查到地址");
        }
        return Response.createBySuccess("更新地址成功",address);
    }


    public Response update(Integer userId, Address address){
        address.setUserId(userId);
        int rowCount = addressMapper.updateByAddress(address);
        if(rowCount > 0){
            return Response.createBySuccess("更新地址成功");
        }
        return Response.createByErrorMessage("更新地址失败");
    }

    public Response<PageInfo> list(Integer userId, int pageNum, int pageSize){
        PageHelper.startPage(pageNum,pageSize);
        List<Address> addressList = addressMapper.selectByUserId(userId);
        PageInfo pageInfo = new PageInfo(addressList);
        return Response.createBySuccess(pageInfo);
    }
}
