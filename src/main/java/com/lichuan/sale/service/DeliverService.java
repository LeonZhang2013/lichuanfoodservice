package com.lichuan.sale.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.lichuan.sale.configurer.StorageStatus;
import com.lichuan.sale.core.CustomException;
import com.lichuan.sale.model.User;
import com.lichuan.sale.tools.Tools;
import com.lichuan.sale.tools.sqltools.MySql;
import com.lichuan.sale.tools.sqltools.Pager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class DeliverService extends BaseService {


    public List<Map<String, Object>> getDeliverProduct(User user) throws CustomException {
        return deliverDao.getDeliverProduct(user);
    }

    public List<Map<String, Object>> getDeliverOrderList(User user, String key) throws CustomException {
        return deliverDao.getDeliverOrderList(user, key);
    }

    public List<Map<String, Object>> getUserOrderInfo(String order_id) {
        return deliverDao.getUserOrderInfo(order_id);
    }

    /**
     * @param deliverJson json 参数 product_id, buy_num, deliver_num
     * @param order_id
     */
    public void deliverOk(String deliverJson, String order_id) {
        deliverDao.deliverOk(deliverJson, order_id);
    }

    public List<Map<String, Object>> getReceiveOrderInfo(Long order_id) {
        return deliverDao.getReceiveOrderInfo(order_id);
    }

    public List<Map<String, Object>> getStorageInfo(Long storageId) throws CustomException {
        List<Map<String, Object>> storageNum = deliverDao.getStorageProductNum(storageId);
        List<Map<String, Object>> orderNum = deliverDao.getUserOrderNum(storageId);
        List<Map<String, Object>> requestNum = storageDao.getStorageOrderRequest(storageId.toString());
        List<Map<String, Object>> waitNum = storageDao.getStorageOrderWait(storageId.toString());
        Tools.mergeMap(storageNum, orderNum, "product_id");
        Tools.mergeMap(storageNum, waitNum, "product_id");
        Tools.mergeMap(storageNum, requestNum, "product_id");
        return storageNum;
    }


    @Transactional
    public void confirmReceive(String dataJson, String log, Long storageId, Long userId, String order_id) throws CustomException {
        JSONArray list = JSON.parseArray(dataJson);
        deliverDao.updateStorageNum(list, storageId);
        deliverDao.confirmReceive(list, log, storageId, userId, order_id);
    }

    public List<Map<String, Object>> receiveList(Pager<Map<String, Object>> pager, Long storage_id) throws CustomException {
        return deliverDao.receiveList(pager, storage_id);
    }

    @Transactional
    public void sendStorageOrder(String dataJson, Long userId, Long storageId) {
        deliverDao.deleteOrder(storageId);
        deliverDao.createOrder(storageId, userId, dataJson);
    }
}
