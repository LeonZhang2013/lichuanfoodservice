package com.lichuan.sale.web;

import com.lichuan.sale.core.CustomException;
import com.lichuan.sale.model.Storage;
import com.lichuan.sale.result.Code;
import com.lichuan.sale.result.SingleResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("storage")
public class StorageController extends BaseController {


    //====================     仓库操作管理   ==========================

    /**
     * 获取 发货管理 订单汇总
     */
    @GetMapping("getStorageOrderAllInOne")
    public SingleResult<Object> getStorageOrderAllInOne() {
        SingleResult<Object> result = new SingleResult<>();
        try {
            List<Map<String, Object>> data = storageService.getTotalStorageNum();
            result.setCode(Code.SUCCESS);
            result.setData(data);
        } catch (Exception e) {
            result.setMessageOfError(e.getMessage());
        }
        return result;
    }


    /**
     * 获取所有仓库列表
     */
    @GetMapping("getStorageListHasOrder")
    public SingleResult<Object> getStorageListHasOrder() {
        SingleResult<Object> result = new SingleResult<>();
        try {
            List<Map<String, Object>> data = storageService.getStorageListHasOrder();
            result.setData(data);
            result.setCode(Code.SUCCESS);
        } catch (Exception e) {
            result.setMessageOfError(e.getMessage());
        }
        return result;
    }

    @GetMapping("getOrderSingleProduct")
    public SingleResult<Object> getOrderSingleProduct(String product_id) {
        SingleResult<Object> result = new SingleResult<>();
        try {
            List<Map<String, Object>> data = storageService.getOrderSingleProduct(product_id);
            result.setCode(Code.SUCCESS);
            result.setData(data);
        } catch (Exception e) {
            result.setMessageOfError(e.getMessage());
        }
        return result;
    }


    @GetMapping("getStorageList")
    public SingleResult<Object> getStorageList() {
        SingleResult<Object> result = new SingleResult<>();
        try {
            List<Map<String, Object>> data = storageService.getStorageList();
            result.setCode(Code.SUCCESS);
            result.setData(data);
        } catch (Exception e) {
            result.setMessageOfError(e.getMessage());
        }
        return result;
    }


    /**
     * 获取单个库房详细订单
     * dataJson   （storage_id  product_id  num）
     */
    @GetMapping("getOrderSingleStorage")
    public SingleResult<Object> getOrderSingleStorage(String storage_id) {
        SingleResult<Object> result = new SingleResult<>();
        try {
            List<Map<String, Object>> data = storageService.getStorageOrderInfo(storage_id);
            result.setCode(Code.SUCCESS);
            result.setData(data);
        } catch (Exception e) {
            result.setMessageOfError(e.getMessage());
        }
        return result;
    }

    /**
     * 分配完毕
     * <p>
     * productJson （order_id, product_id,wait_num）
     */
    @PostMapping("saveProductDispatch")
    public SingleResult<Object> dispatchOrderProductOk(String productJson) {
        SingleResult<Object> result = new SingleResult<>();
        try {
            storageService.dispatchOrderProductOk(productJson);
            result.setCode(Code.SUCCESS);
        } catch (Exception e) {
            result.setMessageOfError(e.getMessage());
        }
        return result;
    }


    /**
     * 分配完毕
     *
     * @param productJson (order_id, product_id,wait_num)
     * @return
     */
    @GetMapping("saveSingleProductDistribution")
    public SingleResult<Object> saveSingleProductDistribution(String productJson) {
        SingleResult<Object> result = new SingleResult<>();
        try {
            storageService.saveSingleProductDistribution(productJson);
            result.setCode(Code.SUCCESS);
        } catch (Exception e) {
            result.setMessageOfError(e.getMessage());
        }
        return result;
    }


    /**
     * 发车
     */
    @PostMapping("sendOutCart")
    public SingleResult<Object> sendOutCart(String storage_id, String cartNo) {
        SingleResult<Object> result = new SingleResult<>();
        try {
            storageService.sendOutCart(getUserId(), storage_id, cartNo);
            result.setCode(Code.SUCCESS);
        } catch (Exception e) {
            result.setMessageOfError(e.getMessage());
        }
        return result;
    }


    //======================  仓库管理  ==================


    /**
     * 获取 仓库管理的 总览
     */
    @GetMapping("getStorageAllInOne")
    public SingleResult<Object> getStorageAllInOne() {
        SingleResult<Object> result = new SingleResult<>();
        try {
            List<Map<String, Object>> data = storageService.getStorageAllInOne();
            result.setCode(Code.SUCCESS);
            result.setData(data);
        } catch (Exception e) {
            result.setMessageOfError(e.getMessage());
        }
        return result;
    }


    /**
     * 获取库房的业务员列表
     */
    @GetMapping("getStorageProxyer")
    public SingleResult<Object> getStorageProxyer(String storageId) {
        SingleResult<Object> result = new SingleResult<>();
        try {
            List<Map<String, Object>> sales = storageService.getSalesByStorageId(storageId);
            result.setData(sales);
            result.setCode(Code.SUCCESS);
        } catch (Exception e) {
            result.setMessageOfError(e.getMessage());
        }
        return result;
    }


    /**
     * 获取单个库房的详情 （这个包含 用户下单）
     *
     * @param storage_id
     * @return
     */

    @GetMapping("getStorageInfo")
    public SingleResult<Object> getStorageInfo(String storage_id) {
        SingleResult<Object> result = new SingleResult<>();
        try {
            List<Map<String, Object>> data = storageService.getStorageInfo(storage_id);
            result.setCode(Code.SUCCESS);
            result.setData(data);
        } catch (Exception e) {
            result.setMessageOfError(e.getMessage());
        }
        return result;
    }


    /**
     * 更新创建库存
     */
    @PostMapping("addStorageProxy")
    public SingleResult<Object> addStorageProxy(String storage_id, String proxy_id) {
        SingleResult<Object> result = new SingleResult<>();
        try {
            storageService.addStorageProxy(storage_id, proxy_id);
            result.setCode(Code.SUCCESS);
        } catch (Exception e) {
            result.setMessageOfError(e.getMessage());
        }
        return result;
    }

    /**
     * 更新创建库存
     */
    @PostMapping("deleteStorageProxy")
    public SingleResult<Object> deleteStorageProxy(String proxy_id) {
        SingleResult<Object> result = new SingleResult<>();
        try {
            if (storageService.getUserCount(proxy_id) == 0) {
                storageService.deleteStorageProxy(proxy_id);
            } else {
                throw new CustomException("先转移客户");
            }

            result.setCode(Code.SUCCESS);
        } catch (Exception e) {
            result.setMessageOfError(e.getMessage());
        }
        return result;
    }

    /**
     * 更新创建库存
     */
    @PostMapping("changeUserProxy")
    public SingleResult<Object> changeUserProxy(String user_id, String proxy_id) {
        SingleResult<Object> result = new SingleResult<>();
        try {
            storageService.changeUserProxy(user_id, proxy_id);
            result.setCode(Code.SUCCESS);
        } catch (Exception e) {
            result.setMessageOfError(e.getMessage());
        }
        return result;
    }


    /**
     * 更新创建库存
     */
    @PostMapping("updateStorage")
    public SingleResult<Object> updateStorage(Storage storage, String proxy_ids) {
        SingleResult<Object> result = new SingleResult<>();
        try {
            storageService.updateStorage(storage, proxy_ids);
            result.setCode(Code.SUCCESS);
        } catch (Exception e) {
            result.setMessageOfError(e.getMessage());
        }
        return result;
    }


    /**
     * 修改仓库状态
     */
    @PostMapping("updateStorageStatus")
    public SingleResult<Object> updateStorageStatus(Long storage_id, String status) {
        SingleResult<Object> result = new SingleResult<>();
        try {
            storageService.updateStorageStatus(storage_id, status);
            result.setCode(Code.SUCCESS);
        } catch (Exception e) {
            result.setMessageOfError(e.getMessage());
        }
        return result;
    }

}
