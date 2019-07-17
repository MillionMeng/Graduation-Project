package com.arvin.controller.Client;

import com.arvin.common.Const;
import com.arvin.common.Response;
import com.arvin.common.ResponseCode;
import com.arvin.pojo.Collect;
import com.arvin.pojo.User;
import com.arvin.service.ICollectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * create by Arvin Meng
 * Date: 2019/5/10.
 */
@Controller
@CrossOrigin(origins = {"http://localhost:8088", "null"})
public class CollectController {

    @Autowired
    private ICollectService collectService;


    /**
     * 收藏商品
     * @param session
     * @param productId
     * @return
     */
    @RequestMapping(value = "/collect/add" ,method = RequestMethod.POST)
    @ResponseBody
    public Response addCollect(HttpSession session,Integer productId){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return Response.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        Collect collect = new Collect();
        collect.setProductId(productId);
        collect.setUserId(user.getId());
        return collectService.addCollect(collect);
    }


    /**
     * 我的收藏列表
     * @param session
     * @return
     */
    @RequestMapping(value = "/collect/list" ,method = RequestMethod.GET)
    @ResponseBody
    public Response CollectList(HttpSession session){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return Response.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return collectService.collectList(user.getId());
    }


    /**
     * 删除商品
     * @param session
     * @param productId
     * @return
     */
    @RequestMapping(value = "/collect/det" ,method = RequestMethod.POST)
    @ResponseBody
    public Response delCollect(HttpSession session,Integer productId){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return Response.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        Collect collect = new Collect();
        collect.setUserId(user.getId());
        collect.setProductId(productId);
        return collectService.delCollect(collect);

    }
}
