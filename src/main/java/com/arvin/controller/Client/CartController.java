package com.arvin.controller.Client;

import com.arvin.common.Const;
import com.arvin.common.Response;
import com.arvin.common.ResponseCode;
import com.arvin.pojo.User;
import com.arvin.service.ICartService;
import com.arvin.vo.CartVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * create by Arvin Meng
 * Date: 2019/4/19.
 */
@Controller
@CrossOrigin(origins = {"http://localhost:8088", "null"})
public class CartController {


    @Autowired
    private ICartService iCartService;

    /**
     * 添加购物车
     * @param session
     * @param count
     * @param productId
     * @return
     */
    @RequestMapping(value = "/cart/add" ,method = RequestMethod.POST)
    @ResponseBody
    public Response<CartVo> add(HttpSession session, Integer count, Integer productId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return Response.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iCartService.add(user.getId(), productId, count);
    }


    /**
     * 更新购物车
     * @param session
     * @param count
     * @param productId
     * @return
     */
    @RequestMapping(value="/cart/update",method = RequestMethod.POST)
    @ResponseBody
    public Response<CartVo> update(HttpSession session, Integer count, Integer productId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return Response.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iCartService.update(user.getId(), productId, count);
    }

    /**
     * 单个删除 或批量删除购物车商品
     * @param session
     * @param productIds
     * @return
     */
    @RequestMapping(value = "/cart/deleteproduct",method = RequestMethod.POST)
    @ResponseBody
    public Response<CartVo> deleteProduct(HttpSession session, String productIds) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return Response.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iCartService.deleteProduct(user.getId(), productIds);
    }

    /**
     * 购物车列表
     * @param session
     * @return
     */
    @RequestMapping(value = "/cart/list",method = RequestMethod.GET)
    @ResponseBody
    public Response<CartVo> list(HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return Response.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iCartService.list(user.getId());
    }

    /**
     * 购物车全选
     * @param session
     * @return
     */
    @RequestMapping(value = "/cart/selectall",method = RequestMethod.POST)
    @ResponseBody
    public Response<CartVo> selectAll(HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return Response.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iCartService.selectOrUnSelect(user.getId(), Const.Cart.CHECKED,null);
    }

    /**
     * 购物车全部取消选择
     * @param session
     * @return
     */
    @RequestMapping(value = "/cart/unselectall",method = RequestMethod.POST)
    @ResponseBody
    public Response<CartVo> unSelectAll(HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return Response.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iCartService.selectOrUnSelect(user.getId(), Const.Cart.UN_CHECKED,null);
    }


    /**
     *购物车单选
     * @param session
     * @param productId
     * @return
     */
    @RequestMapping(value = "/cart/selectone",method = RequestMethod.POST)
    @ResponseBody
    public Response<CartVo> Select(HttpSession session,Integer productId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return Response.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iCartService.selectOrUnSelect(user.getId(), Const.Cart.CHECKED,productId);
    }

    /**
     * 购物车单个取消选择
     * @param session
     * @param productId
     * @return
     */
    @RequestMapping(value = "/cart/unselectone",method = RequestMethod.POST)
    @ResponseBody
    public Response<CartVo> unSelect(HttpSession session,Integer productId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return Response.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iCartService.selectOrUnSelect(user.getId(), Const.Cart.UN_CHECKED,productId);
    }

    /**
     * 查询购物车 商品数量
     * @param session
     * @return
     */
    @RequestMapping(value = "/cart/getproductcount",method = RequestMethod.GET)
    @ResponseBody
    public Response<Integer> getCartProductCount(HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return Response.createBySuccess(0);
        }
        return iCartService.getCartProductCount(user.getId());
    }
}
