package com.lichuan.sale.wx;


import com.lichuan.sale.result.SingleResult;
import com.lichuan.sale.tools.NetUtils;
import com.lichuan.sale.tools.PayUtil;
import com.lichuan.sale.web.BaseController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;
import java.util.Map;

@RestController
@RequestMapping("wx/pay")
public class WxPayController extends BaseController {


    @PostMapping("payment")
    public SingleResult<Object> payment(String order_id, String open_id, HttpServletRequest request) {
        SingleResult<Object> result = new SingleResult<>();
        //获取付款类型
        try {
            String ip = NetUtils.getIpAddr(request);
            Map<String, String> data = wxPayService.getPaymentInfo(open_id, order_id, ip);
            result.setMessageOfSuccess();
            result.setData(data);
        } catch (Exception e) {
            result.setMessageOfError(e.getMessage());
        }
        return result;
    }


    @PostMapping("refund")
    public SingleResult<Object> refund(Long order_id, String ip) {
        SingleResult<Object> result = new SingleResult<>();
        //获取付款类型
        try {
            wxPayService.refund(order_id, ip);
            result.setMessageOfSuccess("申请退款成功");
        } catch (Exception e) {
            result.setMessageOfError(e.getMessage());
        }
        return result;
    }


    @RequestMapping("notifyInfo")
    public void notifyInfo(HttpServletResponse response, HttpServletRequest request) {
        SingleResult<Object> result = new SingleResult<>();
        //获取付款类型
        try {
            String reqParams = NetUtils.getStringFromInputStream(request.getInputStream());
            Map<String, String> mapData = PayUtil.xmlToMap(reqParams);
            if ("SUCCESS".equals(mapData.get("return_code"))) {
                sendWeChatOk(response, "SUCCESS", "");
                String orderId = mapData.get("attach");
                String out_trade_no = mapData.get("out_trade_no");
                orderService.paySuccess(orderId, out_trade_no);
            }
            result.setMessageOfSuccess();
            result.setData(reqParams);
        } catch (Exception e) {
            result.setMessageOfError(e.getMessage());
        }
        //return result;
    }

//    /**
//     * weixing 退款回调 (退款已经成功，回调接口占时没用)
//     * @param response
//     * @param request
//     */
//    @RequestMapping("notifyInfoRefund")
//    public void notifyInfoRefund(HttpServletResponse response,HttpServletRequest request) {
//        SingleResult<Object> result = new SingleResult<>();
//        //获取付款类型
//        try {
//            String reqParams = NetUtils.getStringFromInputStream(request.getInputStream());
//            Map<String, String> mapData = PayUtil.xmlToMap(reqParams);
//            if("SUCCESS".equals(mapData.get("return_code"))){
//                sendWeChatOk(response,"SUCCESS","");
//                String orderId = mapData.get("attach");
//                orderService.refundSuccess(orderId);
//            }
//            result.setMessageOfSuccess();
//            result.setData(reqParams);
//        } catch (Exception e) {
//            result.setMessageOfError(e.getMessage());
//        }
//        //return result;
//    }


    /**
     * 告诉微信接收成功
     *
     * @param response
     * @param returnCode
     * @param returnMessage
     */
    private void sendWeChatOk(HttpServletResponse response, String returnCode, String returnMessage) {
        try {
            response.setContentType("text/xml");
            response.setCharacterEncoding("UTF-8");
            response.setLocale(Locale.SIMPLIFIED_CHINESE);
            PrintWriter printWriter = response.getWriter();
            printWriter.append("<xml>\n");
            printWriter.append("<return_code><![CDATA[");
            printWriter.append(returnCode);
            printWriter.append("]]></return_code>\n");
            printWriter.append("<return_msg><![CDATA[");
            printWriter.append(returnMessage);
            printWriter.append("]]></return_msg>\n");
            printWriter.append("</xml>");
            printWriter.flush();
            printWriter.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }


}
