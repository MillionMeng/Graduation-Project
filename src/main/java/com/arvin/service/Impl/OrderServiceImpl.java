package com.arvin.service.Impl;


import com.alipay.api.AlipayResponse;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.demo.trade.config.Configs;
import com.alipay.demo.trade.model.ExtendParams;
import com.alipay.demo.trade.model.GoodsDetail;
import com.alipay.demo.trade.model.builder.AlipayTradePrecreateRequestBuilder;
import com.alipay.demo.trade.model.result.AlipayF2FPrecreateResult;
import com.alipay.demo.trade.service.AlipayTradeService;
import com.alipay.demo.trade.service.impl.AlipayTradeServiceImpl;
import com.alipay.demo.trade.utils.ZxingUtils;
import com.arvin.common.Const;
import com.arvin.common.Response;
import com.arvin.dao.OrderMapper;
import com.arvin.dao.Order_itemMapper;
import com.arvin.dao.PayInfoMapper;
import com.arvin.pojo.Order;
import com.arvin.pojo.Order_item;
import com.arvin.pojo.PayInfo;
import com.arvin.service.IOrderService;
import com.arvin.util.BigDecimalUtil;
import com.arvin.util.DateTimeUtil;
import com.arvin.util.FTPUtil;
import com.arvin.util.PropertiesUtil;
import com.google.common.collect.Lists;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * create by Arvin Meng
 * Date: 2019/4/26.
 */
@Service("iOrderService")
public class OrderServiceImpl implements IOrderService{


    private static AlipayTradeService tradeService;
    static {
        /** 一定要在创建AlipayTradeService之前调用Configs.init()设置默认参数
         *  Configs会读取classpath下的zfbinfo.properties文件配置信息，如果找不到该文件则确认该文件是否在classpath目录
         */
        Configs.init("zfbinfo.properties");

        /** 使用Configs提供的默认参数
         *  AlipayTradeService可以使用单例或者为静态成员对象，不需要反复new
         */
        tradeService = new AlipayTradeServiceImpl.ClientBuilder().build();
    }


    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private Order_itemMapper order_itemMapper;
    @Autowired
    private PayInfoMapper payInfoMapper;

    public Response pay(Long orderNo,Integer userId,String path){
        Map<String,String> map = new HashMap<>();
        Order order = orderMapper.selectByUserIdAndOrderNo(orderNo,userId);
        if(order == null){
            return Response.createByErrorMessage("用户没有该订单");
        }
        map.put("orderNo",String.valueOf(order.getOrderNo()));



        // (必填) 商户网站订单系统中唯一订单号，64个字符以内，只能包含字母、数字、下划线，
        // 需保证商户系统端不能重复，建议通过数据库sequence生成，
        String outTradeNo =order.getOrderNo().toString();
                //"tradepay" + System.currentTimeMillis() + (long) (Math.random() * 10000000L);

        // (必填) 订单标题，粗略描述用户的支付目的。如“xxx品牌xxx门店消费”
        String subject =new StringBuilder().append("IBuy扫码支付,订单号为:").append(outTradeNo).toString(); /*"xxx品牌xxx门店当面付消费"*/

        // (必填) 订单总金额，单位为元，不能超过1亿元
        // 如果同时传入了【打折金额】,【不可打折金额】,【订单总金额】三者,则必须满足如下条件:【订单总金额】=【打折金额】+【不可打折金额】
        String totalAmount = order.getPayment().toString();

        // (必填) 付款条码，用户支付宝钱包手机app点击“付款”产生的付款条码
        String authCode = "用户自己的支付宝付款码"; // 条码示例，286648048691290423
        // (可选，根据需要决定是否使用) 订单可打折金额，可以配合商家平台配置折扣活动，如果订单部分商品参与打折，可以将部分商品总价填写至此字段，默认全部商品可打折
        // 如果该值未传入,但传入了【订单总金额】,【不可打折金额】 则该值默认为【订单总金额】- 【不可打折金额】
        //        String discountableAmount = "1.00"; //

        // (可选) 订单不可打折金额，可以配合商家平台配置折扣活动，如果酒水不参与打折，则将对应金额填写至此字段
        // 如果该值未传入,但传入了【订单总金额】,【打折金额】,则该值默认为【订单总金额】-【打折金额】
        String undiscountableAmount = "0.0";

        // 卖家支付宝账号ID，用于支持一个签约账号下支持打款到不同的收款账号，(打款到sellerId对应的支付宝账号)
        // 如果该字段为空，则默认为与支付宝签约的商户的PID，也就是appid对应的PID
        String sellerId = "";

        // 订单描述，可以对交易或商品进行一个详细地描述，比如填写"购买商品3件共20.00元"
        String body =new StringBuilder().append("订单").append(outTradeNo).append("购买商品共花费").append(totalAmount).append("元").toString() ;/*"购买商品3件共20.00元";*/

        // 商户操作员编号，添加此参数可以为商户操作员做销售统计
        String operatorId = "test_operator_id";

        // (必填) 商户门店编号，通过门店号和商家后台可以配置精准到门店的折扣信息，详询支付宝技术支持
        String storeId = "test_store_id";

        // 业务扩展参数，目前可添加由支付宝分配的系统商编号(通过setSysServiceProviderId方法)，详情请咨询支付宝技术支持
        String providerId = "2088100200300400500";
        ExtendParams extendParams = new ExtendParams();
        extendParams.setSysServiceProviderId(providerId);

        // 支付超时，定义为120分钟
        String timeoutExpress = "120m";

        // 商品明细列表，需填写购买商品详细信息，
        List<GoodsDetail> goodsDetailList = new ArrayList<GoodsDetail>();

        /**
         * 添加的
         */
        List<Order_item> orderItemList = order_itemMapper.getByOrderNoUserId(orderNo,userId);
        //单位是分 所以要乘以100
        for(Order_item orderItem : orderItemList){
            GoodsDetail goods = GoodsDetail.newInstance(orderItem.getProductId().toString(), orderItem.getProductName(),
                    BigDecimalUtil.mul(orderItem.getCurrentUnitPrice().doubleValue(),new Double(100).doubleValue()).longValue(),
                    orderItem.getQuantity());
            goodsDetailList.add(goods);
        }


        /*// 创建一个商品信息，参数含义分别为商品id（使用国标）、名称、单价（单位为分）、数量，如果需要添加商品类别，详见GoodsDetail
        GoodsDetail goods1 = GoodsDetail.newInstance("goods_id001", "xxx面包", 1000, 1);
        // 创建好一个商品后添加至商品明细列表
        goodsDetailList.add(goods1);

        // 继续创建并添加第一条商品信息，用户购买的产品为“黑人牙刷”，单价为5.00元，购买了两件
        GoodsDetail goods2 = GoodsDetail.newInstance("goods_id002", "xxx牙刷", 500, 2);
        goodsDetailList.add(goods2);
*/

        /**
         * 重要
         */
        // 创建扫码支付请求builder，设置请求参数
        AlipayTradePrecreateRequestBuilder builder = new AlipayTradePrecreateRequestBuilder()
                .setSubject(subject).setTotalAmount(totalAmount).setOutTradeNo(outTradeNo)
                .setUndiscountableAmount(undiscountableAmount).setSellerId(sellerId).setBody(body)
                .setOperatorId(operatorId).setStoreId(storeId).setExtendParams(extendParams)
                .setTimeoutExpress(timeoutExpress)
                .setNotifyUrl(PropertiesUtil.getProperty("alipay.callback.url"))//支付宝服务器主动通知商户服务器里指定的页面http路径,根据需要设置
                .setGoodsDetailList(goodsDetailList);




        AlipayF2FPrecreateResult result = tradeService.tradePrecreate(builder);
        switch (result.getTradeStatus()) {
            case SUCCESS:
                //adasd
                logger.info("支付宝预下单成功: )");

                AlipayTradePrecreateResponse response = result.getResponse();
                dumpResponse(response);

                File file = new File(path);
                if(!file.exists()){
                    file.setWritable(true);
                    file.mkdirs();
                }

                // 需要修改为运行机器上的路径
                //注意/qr-%s.png  的/
                //qrPath 二维码路径
                String qrPath = String.format(path+"/qr-%s.png", response.getOutTradeNo());
                //二维码路径名
                String qrFileName = String.format("qr-%s.png",response.getOutTradeNo());
                ZxingUtils.getQRCodeImge(response.getQrCode(),256,qrPath);

                File targetFile = new File(path,qrFileName);
                try {
                    FTPUtil.uploadFile(Lists.newArrayList(targetFile));
                } catch (IOException e) {
                    logger.error("上传二维码异常",e);
                    e.printStackTrace();
                }

                logger.info("qrPath:" + qrPath);
                String qrUrl = PropertiesUtil.getProperty("ftp.server.http.prefix")+targetFile.getName();
                map.put("qrUrl",qrUrl);

                //                ZxingUtils.getQRCodeImge(response.getQrCode(), 256, filePath);
                return Response.createBySuccess(map);
            case FAILED:
                logger.error("支付宝预下单失败!!!");
                return Response.createByErrorMessage("支付宝预下单失败!!!");

            case UNKNOWN:
                 logger.error("系统异常，预下单状态未知!!!");
                 return Response.createByErrorMessage("系统异常，预下单状态未知!!!");

            default:
                 logger.error("不支持的交易状态，交易返回异常!!!");
                return Response.createByErrorMessage("不支持的交易状态，交易返回异常!!!");

        }
    }


    // 简单打印应答
    private void dumpResponse(AlipayResponse response) {
        if (response != null) {
            logger.info(String.format("code:%s, msg:%s", response.getCode(), response.getMsg()));
            if (StringUtils.isNotEmpty(response.getSubCode())) {
                logger.info(String.format("subCode:%s, subMsg:%s", response.getSubCode(),
                        response.getSubMsg()));
            }
            logger.info("body:" + response.getBody());
        }
    }

    public Response aliCallback(Map<String,String> map){
        Long orderNo =Long.parseLong(map.get("out_trade_no"));
        String tradeNo = map.get("trade_no");
        String tradeStatus = map.get("trade_status");
        Order order = orderMapper.selectByOrderNo(orderNo);
        try{
            if(order == null){
                return Response.createByErrorMessage("不是IBuy的订单，忽略该回调");
            }
            if(order.getStatus() >= Const.OrderStatusEnum.PAID.getCode()){
                return Response.createBySuccess("支付宝重复调用");
            }
            if(Const.AlipayCallback.TRADE_STATUS_TEADE_SUCCESS.equals(tradeStatus)){
                order.setPaymentTime(DateTimeUtil.strToDate(map.get("gmt_payment")));
                order.setStatus(Const.OrderStatusEnum.PAID.getCode());
                orderMapper.updateByPrimaryKeySelective(order);
            }
        }catch (RuntimeException e){
            logger.error("arvin出错",e);
        }


        PayInfo payInfo = new PayInfo();
        payInfo.setUserId(order.getUserId());
        payInfo.setOrderNo(order.getOrderNo());
        payInfo.setPayPlaform(Const.PayPlatformEnum.ALIPAY.getCode());
        payInfo.setPlatformNumber(tradeNo);
        payInfo.setPlatformStatus(tradeStatus);

        payInfoMapper.insert(payInfo);
        return Response.createBySuccess();
    }

    public Response queryOrderPayStatus(Integer userId,Long orderNo){
        Order order = orderMapper.selectByUserIdAndOrderNo(orderNo,userId);
        if(order == null){
            return Response.createByErrorMessage("用户没有订单");
        }

        if(order.getStatus() >= Const.OrderStatusEnum.PAID.getCode()){
            return Response.createBySuccess();
        }
        return Response.createByError();
    }

}
