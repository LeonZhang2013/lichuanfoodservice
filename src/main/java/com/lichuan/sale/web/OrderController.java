package com.lichuan.sale.web;


import com.fasterxml.jackson.databind.ser.Serializers;
import com.lichuan.sale.core.CustomException;
import com.lichuan.sale.result.Code;
import com.lichuan.sale.result.MultiResult;
import com.lichuan.sale.result.SingleResult;
import com.lichuan.sale.tools.Tools;
import com.lichuan.sale.tools.sqltools.Pager;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("order")
public class OrderController extends BaseController{

    @RequestMapping(value = "addOrder", method = RequestMethod.POST)
    public synchronized SingleResult<String> addOrder(Long user_id, Long role_id, String comment, String address_id) {
        SingleResult<String> result = new SingleResult<>();
        result.setCode(Code.ERROR);
        try {
            String proxy_id = userService.getProxyId(user_id);
            if(proxy_id==null){
                throw new CustomException("没有指定配送员");
            }
            Long order_id = Tools.generatorId();
            BigDecimal totalPrice = orderService.addOrder(user_id,order_id,proxy_id);
            String paymentInfo  = orderService.getPaymentInfo(order_id.toString(),totalPrice,"订单付款");
            result.setCode(Code.SUCCESS);
            result.setData(paymentInfo);
        } catch (Exception e) {
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @RequestMapping(value = "getOrderStatus", method = RequestMethod.GET)
    public MultiResult<Map<String, Object>> getOrderStatus() {
        MultiResult<Map<String, Object>> result = new MultiResult<>();
        try {
            List<Map<String, Object>> data = orderService.getOrderStatus();
            result.setMessageOfSuccess();
            result.setData(data);
        } catch (Exception e) {
            result.setMessageOfError(e.getMessage());
        }
        return result;
    }

    @RequestMapping(value = "getOrderList", method = RequestMethod.GET)
    public MultiResult<Map<String, Object>> getOrderList(Integer status, String key, Pager<Map<String, Object>> pager) {
        MultiResult<Map<String, Object>> result = new MultiResult<>();
        try {
            List<Map<String, Object>> data = orderService.getOrderList(status, key, pager, getUser());
            result.setMessageOfSuccess();
            result.setData(data);
        } catch (Exception e) {
            result.setMessageOfError(e.getMessage());
        }
        return result;
    }


    @RequestMapping(value = "getOrderDetail", method = RequestMethod.GET)
    public SingleResult<Object> getOrderDetail(Long order_id, Integer status) {
        SingleResult<Object> result = new SingleResult<>();
        try {
            if (null != order_id) {
                List<Map<String, Object>> orderDetail = orderService.getOrderDetail(order_id, status);
                result.setCode(Code.SUCCESS);
                result.setData(orderDetail);
            } else {
                result.setCode(Code.EXP_PARAM);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.setCode(Code.ERROR);
        }
        return result;
    }


    @RequestMapping(value = "operateOrder", method = RequestMethod.POST)
    public synchronized SingleResult<String> operateOrder(Long order_id, Integer status) {
        SingleResult<String> result = new SingleResult<>();
        try {
            result.setCode(Code.ERROR);
            if (null != order_id && null != status) {
                orderService.operateOrder(order_id, status);
                result.setCode(Code.SUCCESS);
            } else {
                result.setCode(Code.EXP_PARAM);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @RequestMapping(value = "cancelOrder", method = RequestMethod.POST)
    public synchronized SingleResult<String> cancelOrder(Long order_id) {
        SingleResult<String> result = new SingleResult<>();
        try {
            orderService.cancelOrder(order_id,getUserId());
            result.setCode(Code.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            result.setCode(Code.ERROR);
        }
        return result;
    }
}
