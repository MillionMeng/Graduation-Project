package com.arvin.controller.Client;

import com.arvin.common.Const;
import com.arvin.common.Response;
import com.arvin.common.ResponseCode;
import com.arvin.pojo.Address;
import com.arvin.pojo.User;
import com.arvin.service.IAddressService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

/**
 * create by Arvin Meng
 * Date: 2019/4/21.
 */
@Controller
@CrossOrigin(origins = {"http://localhost:8088", "null"})
public class AdressController {

    @Autowired
    private IAddressService iAddressService;

    /**
     * 新建收获地址
     * @param session
     * @param address
     * @return
     */
    @RequestMapping(value="address/add",method = RequestMethod.POST)
    @ResponseBody
    public Response add(HttpSession session, @RequestBody Address address){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return Response.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iAddressService.add(user.getId(),address);
    }

    /**
     * 删除收货地址
     * @param session
     * @param addressId
     * @return
     */
    @RequestMapping(value="address/del")
    @ResponseBody
    public Response del(HttpSession session, Integer addressId){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return Response.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iAddressService.del(user.getId(),addressId);
    }


    /**
     * 查看收货地址
     * @param session
     * @param addressId
     * @return
     */
    @RequestMapping(value="address/select")
    @ResponseBody
    public Response<Address> select(HttpSession session, Integer addressId){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return Response.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iAddressService.select(user.getId(),addressId);
    }


    /**
     * 更新收货地址
     * @param session
     * @param address
     * @return
     */
    @RequestMapping(value="address/update",method = RequestMethod.POST)
    @ResponseBody
    public Response update(HttpSession session,@RequestBody Address address){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return Response.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iAddressService.update(user.getId(),address);
    }

    /**
     * 收货地址列表
     * @param pageNum
     * @param pageSize
     * @param session
     * @return
     */
    @RequestMapping(value="address/list")
    @ResponseBody
    public Response<PageInfo> list(@RequestParam(value = "pageNum",defaultValue = "1") int pageNum,
                                   @RequestParam(value = "pageSize",defaultValue = "10") int pageSize,
                                   HttpSession session){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return Response.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iAddressService.list(user.getId(),pageNum,pageSize);
    }
}
