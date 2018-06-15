package com.lichuan.sale.service.wx;


import com.lichuan.sale.configurer.XCXInfo;
import com.lichuan.sale.model.User;
import com.lichuan.sale.service.BaseService;
import com.lichuan.sale.tools.PayUtil;
import com.lichuan.sale.tools.Tools;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
public class WxPayService extends BaseService {

    public Map<String,String> getPaymentInfo(User user, String order_id, String ip) throws Exception {
        String payPrice = orderDao.getOrderPrice(order_id);
        return getPaymentInfo(user.getXcx_id(),order_id,payPrice,"订单付款",ip);
    }

    /**
     *
     * @param xcxId
     * @param orderId
     * @param payMoney
     * @param desc
     * @param ip
     * @return
     * @throws Exception
     */
    public  Map<String, String> getPaymentInfo(String xcxId,String orderId, String payMoney, String desc,String ip) throws Exception {
        //微信付款 是 以 分为单位。所有要将元* 100
        payMoney = new BigDecimal(payMoney).multiply(new BigDecimal(100)).setScale(0).toString();
        //test:
        payMoney = "1";
        String nonce_str = PayUtil.generateNonceStr();
        Long aLong = Tools.generatorId();
        //组装参数，用户生成统一下单接口的签名
        Map<String, String> packageParams = new HashMap ();
        packageParams.put("appid", XCXInfo.getAPPID());
        packageParams.put("mch_id", XCXInfo.getMCHID());
        packageParams.put("nonce_str", nonce_str);
        packageParams.put("body", desc);
        packageParams.put("out_trade_no", aLong.toString());//商户订单号
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
        if (return_code == "SUCCESS" || return_code.equals(return_code)) {
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
