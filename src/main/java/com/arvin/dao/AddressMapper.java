package com.arvin.dao;

import com.arvin.pojo.Address;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AddressMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Address record);

    int insertSelective(Address record);

    Address selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Address record);

    int updateByPrimaryKey(Address record);

    int deleteByAddressIdUserId(@Param("userId") Integer userId, @Param("addressId") Integer addressId);

    Address selectByAddressIdUserId(@Param("userId") Integer userId,@Param("addressId") Integer addressId);

    int updateByAddress(Address record);

    List<Address> selectByUserId(@Param("userId") Integer userId);




}