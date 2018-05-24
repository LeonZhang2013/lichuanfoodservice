package com.lichuan.sale.web;

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
    @GetMapping("getDeliverProduct ")
    public SingleResult<Object> getDeliverProduct() {
        SingleResult<Object> result = new SingleResult<>();
        result.setCode(Code.ERROR);
        try {
            result.setCode(Code.SUCCESS);
        } catch (Exception e) {
            result.setMessageOfError(e.getMessage());
        }
        return result;
    }

    @GetMapping("getUserOrderList ")
    public SingleResult<Object> getUserOrderList () {
        SingleResult<Object> result = new SingleResult<>();
        result.setCode(Code.ERROR);
        try {
            result.setCode(Code.SUCCESS);
        } catch (Exception e) {
            result.setMessageOfError(e.getMessage());
        }
        return result;
    }

    @GetMapping("getUserOrderIofo ")
    public SingleResult<Object> getUserOrderIofo (String order_id, String status) {
        SingleResult<Object> result = new SingleResult<>();
        try {
            result.setCode(Code.SUCCESS);
        } catch (Exception e) {
            result.setMessageOfError(e.getMessage());
        }
        return result;
    }

    @PostMapping("deliverOk")
    public SingleResult<String> deliverOk (String deliverJson,String order_id) {
        SingleResult<String> result = new SingleResult<>();
        try {
            result.setCode(Code.SUCCESS);
        } catch (Exception e) {
            result.setMessageOfError(e.getMessage());
        }
        return result;
    }


}
