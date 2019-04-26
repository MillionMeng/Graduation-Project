package com.arvin.controller.Client;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.demo.trade.config.Configs;
import com.arvin.common.Const;
import com.arvin.common.Response;
import com.arvin.common.ResponseCode;
import com.arvin.pojo.User;
import com.arvin.service.IOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * create by Arvin Meng
 * Date: 2019/4/26.
 */
@Controller
@CrossOrigin(origins = {"http://localhost:8088", "null"})
public class OrderController {

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private IOrderService iOrderService;

    /**
     * 支付接口
     *
     * @param session
     * @param orderNo
     * @param request
     * @return
     */
    @RequestMapping(value = "order/pay", method = RequestMethod.POST)
    @ResponseBody
    public Response pay(HttpSession session, @RequestParam Long orderNo, HttpServletRequest request) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return Response.createBySuccess(user);
        }
        //二维码路径
        String path = request.getSession().getServletContext().getRealPath("upload");
        return iOrderService.pay(orderNo, user.getId(), path);
    }

    /**
     * 支付宝处理回调
     * @param request
     * @return
     */
    @RequestMapping(value = "/order/alipay_callback.do")
    @ResponseBody
    public Object callBack(HttpServletRequest request){
        Map<String,String> map = new HashMap<>();

        //支付宝将参数都放在request里  需要从request里取
        Map requestParameterMap = request.getParameterMap();
        //动态查询 key和value都是什么  value是放到数组里的 需要取出数组里的value值
        for(Iterator iter = requestParameterMap.keySet().iterator();iter.hasNext();){
            //先取出name
            String name = (String)iter.next();
            //再取数组
            String[] values = (String[]) requestParameterMap.get(name);
            String valueStr ="";
            //遍历数组
            for(int i = 0 ;i < values.length ; i++){
                //要对length进行判断    多个元素 用,进行分割
                valueStr = (i == values.length-1)?valueStr + values[i] : valueStr + values[i] + "," ;
            }

            map.put(name,valueStr);
        }
        logger.info("支付宝回调，sign:{},trade_status:{},参数:{}",map.get("sign"),map.get("trade_status"),map.toString());

        //非常重要，验证回调的正确性，是不是支付宝发的，并且还有避免重复通知
        try{
            map.remove("sign_type");
            try {
                boolean alipayRSACheckedV2 = AlipaySignature.rsaCheckV2(map,Configs.getAlipayPublicKey(),"utf-8",Configs.getSignType());

                if(!alipayRSACheckedV2){
                    return Response.createByErrorMessage("非法请求，验证不通过");
                }
            } catch (AlipayApiException e) {
                logger.error("支付宝验证回调异常",e);
                e.printStackTrace();
            }
        }catch (RuntimeException e){
            logger.error("Controller支付宝验证回调异常",e);
        }


        //todo 验证各种数据

        Response response = iOrderService.aliCallback(map);

        if(response.isSuccess()){
            return Const.AlipayCallback.RESPONSE_SUCCESS;
        }
        return Const.AlipayCallback.RESPONSE_FAILED;

    }

    /**
     * 前端轮询查询订单支付状态
     * @param session
     * @param orderNo
     * @return
     */
    @RequestMapping("/order/query_order_pay_status")
    @ResponseBody
    public Response<Boolean> queryOrderPayStatus(HttpSession session, Long orderNo){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return Response.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        Response serverResponse =  iOrderService.queryOrderPayStatus(user.getId(),orderNo);
        if(serverResponse.isSuccess()){
            return Response.createBySuccess(true);
        }
        return Response.createBySuccess(false);
    }

}
