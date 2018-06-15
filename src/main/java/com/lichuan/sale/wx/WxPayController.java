package com.lichuan.sale.wx;


import com.lichuan.sale.result.SingleResult;
import com.lichuan.sale.tools.NetUtils;
import com.lichuan.sale.tools.PayUtil;
import com.lichuan.sale.web.BaseController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sun.nio.ch.Net;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

@RestController
@RequestMapping("wx/pay")
public class WxPayController extends BaseController {


    @PostMapping("payment")
    public SingleResult<Object> payment(String order_id, HttpServletRequest request){
        SingleResult<Object> result = new SingleResult<>();
        //获取付款类型
        try {
            String ip = NetUtils.getIpAddr(request);
            Map<String,String> data = wxPayService.getPaymentInfo(getUser(),order_id,ip);
            result.setMessageOfSuccess();
            result.setData(data);
        } catch (Exception e) {
            result.setMessageOfError(e.getMessage());
        }
        return result;
    }


    @RequestMapping("notifyInfo")
    public SingleResult<Object> notifyInfo(HttpServletRequest request) {
        SingleResult<Object> result = new SingleResult<>();
        //获取付款类型
        try {
            String reqParams = NetUtils.getStringFromInputStream(request.getInputStream());
            Map<String, String> mapData = PayUtil.xmlToMap(reqParams);
            if("SUCCESS".equals(mapData.get("return_code"))){
                String orderId = mapData.get("attach");
                orderService.paySuccess(orderId);
            }
            result.setMessageOfSuccess();
            result.setData(reqParams);
        } catch (Exception e) {
            result.setMessageOfError(e.getMessage());
        }
        return result;
    }


}
