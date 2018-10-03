package com.lichuan.sale.service.wx;


import com.lichuan.sale.Application;
import com.lichuan.sale.configurer.XCXInfo;
import com.lichuan.sale.core.CustomException;
import com.lichuan.sale.service.BaseService;
import com.lichuan.sale.tools.PayUtil;
import com.lichuan.sale.tools.Tools;
import com.lichuan.sale.constant.LcConstant;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class WxPayService extends BaseService {

    public Map<String, String> getPaymentInfo(String open_id, String order_id, String ip) throws Exception {
        String payPrice = orderDao.getOrderPrice(order_id);
        return getPaymentInfo(open_id, order_id, payPrice, "订单付款", ip);
    }


    //退款
    public Map<String, String> refund(Long order_id, String ip) throws Exception {

        //微信付款 是 以 分为单位。所有要将元* 100
        if (ip == null) ip = "192.168.0.1";
        String nonce_str = PayUtil.generateNonceStr();
        Map<String, Object> orderDetail = orderDao.getOrderDetail(order_id);
        System.out.println(orderDetail);
        Date create_time = (Date) orderDetail.get("create_time");
        Long offsetHour = (System.currentTimeMillis() - create_time.getTime()) / 1000 / 2600;
        if (offsetHour > LcConstant.REFUND_ENABLE) { //如果大于4小时,就不让退货
            throw new CustomException("你的订单已经在路上了");
        }
        String totalPrice = orderDetail.get("total_price").toString();
        if(Application.Test)totalPrice = "1";
        //组装参数，用户生成统一下单接口的签名
        Map<String, String> packageParams = new HashMap();
        packageParams.put("appid", XCXInfo.APPID);
        packageParams.put("mch_id", XCXInfo.PAY_MCH_ID);
        packageParams.put("nonce_str", nonce_str);

        packageParams.put("out_trade_no", orderDetail.get("platform_no").toString());//退款订单号
        packageParams.put("out_refund_no", order_id.toString());
        packageParams.put("total_fee", totalPrice);//支付金额，这边需要转成字符串类型，否则后面的签名会失败
        packageParams.put("refund_fee", totalPrice);//退款金额
        packageParams.put("spbill_create_ip", ip);
        packageParams.put("notify_url", XCXInfo.REFUND_INFO_URL);//退款完成后调用的地址

        String signedXml = PayUtil.generateSignedXml(packageParams, XCXInfo.PAY_KEY);
        String resultData = PayUtil.httpPostSSL(XCXInfo.REFUND_URL, signedXml);
        Map map = PayUtil.doXMLParse(resultData);


        String return_code = (String) map.get("return_code");//返回状态码
        Map<String, String> data = new HashMap<>();//返回给小程序端需要的参数
        if (return_code.equals("SUCCESS")) {
            String orderId = map.get("out_refund_no").toString();
            orderDao.cancelOrder(orderId);
        } else {
            throw new CustomException(map.get("err_code_des").toString());
        }
        return data;
    }


    /**
     * @param xcxId
     * @param orderId
     * @param payMoney
     * @param desc
     * @param ip
     * @return
     * @throws Exception
     */
    public Map<String, String> getPaymentInfo(String xcxId, String orderId, String payMoney, String desc, String ip) throws Exception {
        //微信付款 是 以 分为单位。所有要将元* 100
        payMoney = new BigDecimal(payMoney).multiply(new BigDecimal(100)).setScale(0).toString();
        //test:
        if(Application.Test)payMoney = "1";
        String nonce_str = PayUtil.generateNonceStr();
        //组装参数，用户生成统一下单接口的签名
        Map<String, String> packageParams = new HashMap();
        packageParams.put("appid", XCXInfo.APPID);
        packageParams.put("mch_id", XCXInfo.PAY_MCH_ID);
        packageParams.put("nonce_str", nonce_str);
        packageParams.put("body", desc);
        packageParams.put("out_trade_no", Tools.generatorId().toString());//weixin支付流水号，回调接口时使用。
        packageParams.put("attach", orderId);//商户订单号
        packageParams.put("total_fee", payMoney);//支付金额，这边需要转成字符串类型，否则后面的签名会失败
        packageParams.put("spbill_create_ip", ip);
        packageParams.put("notify_url", XCXInfo.PAY_NOTIFY_URL);//支付成功后的回调地址
        packageParams.put("trade_type", XCXInfo.PAY_TRADETYPE);//支付方式
        packageParams.put("openid", xcxId);

        String signedXml = PayUtil.generateSignedXml(packageParams, XCXInfo.PAY_KEY);

        String resultData = PayUtil.httpRequest(XCXInfo.PAY_URL, "POST", signedXml);

        Map map = PayUtil.doXMLParse(resultData);
        String return_code = (String) map.get("return_code");//返回状态码
        String timeStamp = String.valueOf(PayUtil.getCurrentTimestamp());

        Map<String, String> data = new HashMap<>();//返回给小程序端需要的参数
        if (return_code.equals("SUCCESS")) {
            data.put("appId", (String) map.get("appid"));
            data.put("nonceStr", (String) map.get("nonce_str"));
            data.put("package", "prepay_id=" + map.get("prepay_id"));
            data.put("timeStamp", timeStamp);
            data.put("signType", "MD5");
            String signature = PayUtil.generateSignature(data, XCXInfo.PAY_KEY);
            data.put("paySign", signature);
        }
        data.put("payState", "wx");
        return data;
    }


}
