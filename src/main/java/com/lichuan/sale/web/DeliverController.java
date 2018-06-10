package com.lichuan.sale.web;

import com.lichuan.sale.core.CustomException;
import com.lichuan.sale.model.Product;
import com.lichuan.sale.result.Code;
import com.lichuan.sale.result.MultiResult;
import com.lichuan.sale.result.SingleResult;
import com.lichuan.sale.tools.sqltools.Pager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("deliver")
public class DeliverController extends BaseController {

    /**
     * 获取订购数量和库存
     */
    @GetMapping("getDeliverProduct")
    public SingleResult<Object> getDeliverProduct() {
        SingleResult<Object> result = new SingleResult<>();
        try {
            if(getStorageId()==null || getStorageId() == 0){
                throw new CustomException("未分配仓库");
            }
            List<Map<String,Object>> deliverData = deliverService.getDeliverProduct(getUserId());
            result.setData(deliverData);
            result.setCode(Code.SUCCESS);
        } catch (Exception e) {
            result.setMessageOfError(e.getMessage());
        }
        return result;
    }

    @GetMapping("getDeliverOrderList")
    public SingleResult<Object> getDeliverOrderList (String key) {
        SingleResult<Object> result = new SingleResult<>();
        result.setCode(Code.ERROR);
        try {
            List<Map<String,Object>> orderList = deliverService.getDeliverOrderList(getUserId(),key);
            result.setCode(Code.SUCCESS);
            result.setData(orderList);
        } catch (Exception e) {
            result.setMessageOfError(e.getMessage());
        }
        return result;
    }


    /**
     * 获取用户订单详情
     * @param order_id
     * @return
     */
    @GetMapping("getUserOrderInfo")
    public SingleResult<Object> getUserOrderInfo (String order_id) {
        SingleResult<Object> result = new SingleResult<>();
        try {
            List<Map<String,Object>> order =  deliverService.getUserOrderInfo(order_id);
            result.setCode(Code.SUCCESS);
            result.setData(order);
        } catch (Exception e) {
            result.setMessageOfError(e.getMessage());
        }
        return result;
    }

    /**
     * 配送完毕
     * @param deliverJson  json 参数 product_id, buy_num, deliver_num
     * @param order_id
     * @return
     */
    @PostMapping("deliverOk")
    public SingleResult<String> deliverOk (String deliverJson,String order_id) {
        SingleResult<String> result = new SingleResult<>();
        try {
            deliverService.deliverOk(deliverJson,order_id);
            result.setCode(Code.SUCCESS);
        } catch (Exception e) {
            result.setMessageOfError(e.getMessage());
        }
        return result;
    }


    //收货： 获取数据
    @GetMapping("getReceiveOrderInfo")
    public SingleResult<Object> getReceiveOrderInfo (Long order_id) {
        SingleResult<Object> result = new SingleResult<>();
        try {
            List<Map<String,Object>> data = deliverService.getReceiveOrderInfo(order_id);
            result.setCode(Code.SUCCESS);
            result.setData(data);
        } catch (Exception e) {
            result.setMessageOfError(e.getMessage());
        }
        return result;
    }

    //收货： 确实收货
    @PostMapping("confirmReceive")
    public SingleResult<String> confirmReceive(String order_id,String dataJson,String log) {
        SingleResult<String> result = new SingleResult<>();
        try {
            if(log == null) log = "";
            deliverService.confirmReceive(dataJson,log,getStorageId(),getUserId(),order_id);
            result.setCode(Code.SUCCESS);
        } catch (Exception e) {
            result.setMessageOfError(e.getMessage());
        }
        return result;
    }

    //收货： 车次列表
    @GetMapping("getReceiveList")
    public SingleResult<Object> receiveList(Pager<Map<String, Object>> pager) {
        SingleResult<Object> result = new SingleResult<>();
        try {
            List<Map<String, Object>> maps = deliverService.receiveList(pager,getStorageId());
            result.setCode(Code.SUCCESS);
            result.setData(maps);
        } catch (Exception e) {
            result.setMessageOfError(e.getMessage());
        }
        return result;
    }

    //补仓  回显 数据

    /**
     *
     * @return
     */
    @GetMapping("getStorageInfo")
    public SingleResult<Object> getStorageInfo () {
        SingleResult<Object> result = new SingleResult<>();
        try {
            List<Map<String,Object>> data = deliverService.getStorageInfo(getStorageId());
            result.setCode(Code.SUCCESS);
            result.setData(data);
        } catch (Exception e) {
            result.setMessageOfError(e.getMessage());
        }
        return result;
    }


    //补仓： 发送不传数据。
    @PostMapping("sendStorageOrder")
    public SingleResult<String> sendStorageOrder(String dataJson) {
        SingleResult<String> result = new SingleResult<>();
        try {
            deliverService.sendStorageOrder(dataJson,getUserId(),getStorageId());
            result.setCode(Code.SUCCESS);
        } catch (Exception e) {
            result.setMessageOfError(e.getMessage());
        }
        return result;
    }


}
