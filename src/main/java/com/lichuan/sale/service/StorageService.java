package com.lichuan.sale.service;

import com.lichuan.sale.core.CustomException;
import com.lichuan.sale.model.Storage;
import com.lichuan.sale.tools.Tools;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class StorageService extends BaseService {

    public void updateStorage(Storage storage, String proxy_ids) throws CustomException {
        storageDao.updateStorage(storage,proxy_ids);
    }

    public List<Map<String,Object>> getStorageListHasOrder() {
        return storageDao.getStorageListHasOrder();
    }

    public void updateStorageStatus(Long storage_id, String status) throws CustomException {
         storageDao.updateStorageStatus(storage_id,status);
    }


    /**
     * 库房信息 和 库房发给公司的请求信息
     * @param storage_id
     * @return
     * @throws CustomException
     */
    public List<Map<String,Object>> getStorageOrderInfo(String storage_id) throws CustomException {
        List<Map<String, Object>> orderInfo = storageDao.getRequestOrderNum(storage_id);
        return orderInfo;
    }

    /**
     * 汇总  库房信息 和 库房发给公司的请求信息
     * @return
     * @throws CustomException
     */
    public List<Map<String,Object>> getTotalStorageNum() throws CustomException {
        List<Map<String, Object>> orderInfo = storageDao.getRequestOrderNum();
        return orderInfo;
    }


    public void dispatchOrderProductOk(String productJson) {
        storageDao.updateOrder(productJson);
    }

    public List<Map<String,Object>> getSalesByStorageId(String storageId) {
        return storageDao.getSalesByStorageId(storageId);
    }

    public void sendOutCart(Long userId, String storage_id, String cartNo) {
        storageDao.sendOutCart(userId,storage_id,cartNo);
    }

    public List<Map<String,Object>> getStorageInfo(String storage_id) throws CustomException {
        List<Map<String, Object>> storageOrderInfo = storageDao.getStorageOrderInfo(storage_id);
        List<Map<String, Object>> sendOrder = storageDao.getNotCompleteOrderNum(storage_id);
        List<Map<String, Object>> orderProduct = deliverDao.getUserOrderNum(Long.parseLong(storage_id));
        Tools.mergeMap(storageOrderInfo,sendOrder,"product_id");
        Tools.mergeMap(storageOrderInfo,orderProduct,"product_id");
        return storageOrderInfo;
    }

    public List<Map<String,Object>> getStorageAllInOne() throws CustomException {
        List<Map<String, Object>> totalStorageInfo = storageDao.getTotalStorageNum();
        List<Map<String, Object>> orderInfo = storageDao.getNotCompleteOrderNum(null);
        List<Map<String, Object>> orderProduct = deliverDao.getUserOrderNum(null);
        Tools.mergeMap(totalStorageInfo,orderInfo,"product_id");
        Tools.mergeMap(totalStorageInfo,orderProduct,"product_id");
        return totalStorageInfo;
    }

    public List<Map<String,Object>> getOrderSingleProduct(String product_id) throws CustomException {
       return storageDao.getOrderSingleProduct(product_id);
    }

    public void saveSingleProductDistribution(String dataJson) {
        storageDao.updateOrder(dataJson);
    }

    public List<Map<String,Object>> getStorageList() {
       return storageDao.getStorageList();
    }
}
