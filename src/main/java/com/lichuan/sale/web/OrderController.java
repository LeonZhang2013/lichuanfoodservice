package com.lichuan.sale.web;


import com.fasterxml.jackson.databind.ser.Serializers;
import com.lichuan.sale.core.CustomException;
import com.lichuan.sale.result.Code;
import com.lichuan.sale.result.MultiResult;
import com.lichuan.sale.result.SingleResult;
import com.lichuan.sale.tools.NetUtils;
import com.lichuan.sale.tools.Tools;
import com.lichuan.sale.tools.sqltools.Pager;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("order")
public class OrderController extends BaseController{

    @RequestMapping(value = "createOrder", method = RequestMethod.POST)
    public synchronized SingleResult<Object> addOrder(Long user_id, String remark, String address_id,HttpServletRequest request) {
        SingleResult<Object> result = new SingleResult<>();
        result.setCode(Code.ERROR);
        try {
            String proxy_id = userService.getProxyId(user_id);
            if(proxy_id==null){
                throw new CustomException("没有指定配送员");
            }
            Long order_id = Tools.generatorId();
            BigDecimal totalPrice = orderService.createOrder(user_id,order_id,proxy_id,remark,address_id);
            String ip = NetUtils.getIpAddr(request);
            Map<String, String> paymentInfo = wxPayService.getPaymentInfo(getUser().getXcx_id(), order_id.toString(), totalPrice.toString(), "订单付款", ip);
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

    @GetMapping("getOrderList")
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

    @GetMapping("getWxOrderList")
    public MultiResult<Map<String, Object>> getWxOrderList(Integer status, Pager<Map<String, Object>> pager) {
        MultiResult<Map<String, Object>> result = new MultiResult<>();
        try {
            List<Map<String, Object>> data = orderService.getWxOrderList(status, pager, getUserId());
            result.setMessageOfSuccess();
            result.setData(data);
        } catch (Exception e) {
            result.setMessageOfError(e.getMessage());
        }
        return result;
    }


    @GetMapping("getProductSaleNum")
    public SingleResult<Object> getProductSaleNum(Long product_id){
        SingleResult<Object> result = new SingleResult<>();
        try {
            if (null != product_id) {
                Map<String, Object> productSaleNum = orderService.getProductSaleNum(product_id);
                result.setCode(Code.SUCCESS);
                result.setData(productSaleNum);
            } else {
                result.setCode(Code.EXP_PARAM);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.setCode(Code.ERROR);
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

    @GetMapping("getSaleNum")
    public SingleResult<Object> getSaleNum(String product_id) {
        SingleResult<Object> result = new SingleResult<>();
        try {
            if (null != product_id) {
                Object saleNum = orderService.getSaleNum(product_id);
                result.setCode(Code.SUCCESS);
                result.setData(saleNum);
            } else {
                result.setCode(Code.EXP_PARAM);
            }
        } catch (Exception e) {
            result.setMessageOfError(e.getMessage());
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
