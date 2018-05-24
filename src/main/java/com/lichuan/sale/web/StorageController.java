package com.lichuan.sale.web;

import com.lichuan.sale.result.Code;
import com.lichuan.sale.result.SingleResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("storage")
public class StorageController extends BaseController {


    /**
     * 获取订购数量和库存
     */
    @GetMapping("getNewStorageOrder")
    public SingleResult<Object> getNewStorageOrder() {
        SingleResult<Object> result = new SingleResult<>();
        result.setCode(Code.ERROR);
        try {
            result.setCode(Code.SUCCESS);
        } catch (Exception e) {
            result.setMessageOfError(e.getMessage());
        }
        return result;
    }

    /**
     * 获取订购数量和库存
     */
    @GetMapping("getStorageOrderList")
    public SingleResult<Object> getStorageOrderList() {
        SingleResult<Object> result = new SingleResult<>();
        result.setCode(Code.ERROR);
        try {
            result.setCode(Code.SUCCESS);
        } catch (Exception e) {
            result.setMessageOfError(e.getMessage());
        }
        return result;
    }


    /**
     * 获取订购数量和库存
     */
    @GetMapping("getStorageOrderInfo")
    public SingleResult<Object> getStorageOrderInfo(String storage_id) {
        SingleResult<Object> result = new SingleResult<>();
        result.setCode(Code.ERROR);
        try {
            result.setCode(Code.SUCCESS);
        } catch (Exception e) {
            result.setMessageOfError(e.getMessage());
        }
        return result;
    }


    /**
     * 获取订购数量和库存
     * dataJson   （storage_id  product_id  num）
     */
    @PostMapping("dispatchOrder")
    public SingleResult<Object> getNewStorageOrder(String dataJson) {
        SingleResult<Object> result = new SingleResult<>();
        result.setCode(Code.ERROR);
        try {
            result.setCode(Code.SUCCESS);
        } catch (Exception e) {
            result.setMessageOfError(e.getMessage());
        }
        return result;
    }


    /**
     * 获取订购数量和库存
     */
    @PostMapping("dispatchOk")
    public SingleResult<Object> dispatchOk(String storage_id) {
        SingleResult<Object> result = new SingleResult<>();
        result.setCode(Code.ERROR);
        try {
            result.setCode(Code.SUCCESS);
        } catch (Exception e) {
            result.setMessageOfError(e.getMessage());
        }
        return result;
    }

}
