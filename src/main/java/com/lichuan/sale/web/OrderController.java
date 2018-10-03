package com.lichuan.sale.web;


import com.lichuan.sale.result.Code;
import com.lichuan.sale.result.MapResult;
import com.lichuan.sale.result.MultiResult;
import com.lichuan.sale.result.SingleResult;
import com.lichuan.sale.tools.Arith;
import com.lichuan.sale.tools.NetUtils;
import com.lichuan.sale.tools.Tools;
import com.lichuan.sale.tools.sqltools.Pager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("order")
public class OrderController extends BaseController {

    @RequestMapping(value = "createOrder", method = RequestMethod.POST)
    public synchronized SingleResult<Object> addOrder(Long user_id, String open_id, String remark, String address_id, HttpServletRequest request) {
        SingleResult<Object> result = new SingleResult<>();
        result.setCode(Code.ERROR);
        try {
            Long order_id = Tools.generatorId();
            BigDecimal totalPrice = orderService.createOrder(getUser(), order_id, remark, address_id);
            String ip = NetUtils.getIpAddr(request);
            Map<String, String> paymentInfo = wxPayService.getPaymentInfo(open_id, order_id.toString(), totalPrice.toString(), "订单付款", ip);
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

    //确认收货
    @RequestMapping(value = "goodsOk", method = RequestMethod.POST)
    public MultiResult<Map<String, Object>> goodsOk(Long order_id) {
        MultiResult<Map<String, Object>> result = new MultiResult<>();
        try {
            String message = orderService.goodsOk(order_id);
            result.setMessageOfSuccess(message);
        } catch (Exception e) {
            result.setMessageOfError(e.getMessage());
        }
        return result;
    }


    @GetMapping("getOrderList")
    public MultiResult<Map<String, Object>> getOrderList(Integer status, String key, Pager<Map<String, Object>> pager) {
        MultiResult<Map<String, Object>> result = new MultiResult<>();
        try {
            List<Map<String, Object>> data = orderService.getOrderList(status, key, pager, getSysUser());
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
    public SingleResult<Object> getProductSaleNum(Long product_id) {
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
                List<Map<String, Object>> orderDetail = orderService.getOrderItem(order_id, status);
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

    @RequestMapping(value = "getWxOrderDetail", method = RequestMethod.GET)
    public MapResult getWxOrderDetail(Long order_id) {
        MapResult result = new MapResult();
        try {
            if (null != order_id) {
                Map<String, Object> order = orderService.getOrderDetail(order_id);
                List<Map<String, Object>> goods = orderService.getOrderItem(order_id);
                Map<String, Object> charge = Arith.calculationCharge(goods,getUser().getVip());
                result.setCode(Code.SUCCESS);
                result.put("order", order);
                result.put("goods", goods);
                result.put("charge", charge);
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
            orderService.cancelOrder(order_id);
            result.setCode(Code.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            result.setCode(Code.ERROR);
        }
        return result;
    }
}
