package com.lichuan.sale.service;


import com.lichuan.sale.core.CustomException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class StatisticsService extends BaseService {

    final int storage = 0, proxy = 1, user = 2;

    public List<Map<String, Object>> getTimeData(int level, String id, String proxy_id, String category_id, boolean isFright, boolean isUnit) throws CustomException {
        //年
        if (level == 0) {
            return statisticsDao.getTimeData(id, proxy_id, category_id, isFright, isUnit);
        } else {//月
            return statisticsDao.getTimeData(id, proxy_id, category_id, isFright, isUnit);
        }
    }

    public List<Map<String, Object>> getBusinessData(int storageOrProxy, String id, String category_id, String startTime, String endTime, boolean isFright, boolean isUnit) throws CustomException {
        List<Map<String, Object>> data = null;
        switch (storageOrProxy) {
            case storage:
                data = statisticsDao.getStorageData(category_id, startTime, endTime, isFright, isUnit);
                break;
            case proxy:
                data = statisticsDao.getProxyData(category_id, startTime, endTime, isFright, isUnit);
                break;
            case user:
                data = statisticsDao.getUserData(id, category_id, startTime, endTime, isFright, isUnit);
                break;
        }
        return data;
    }

    public List<Map<String, Object>> getProductData(String storage_id, String startTime, String endTime, boolean isFright, boolean isUnit) throws CustomException {
        return statisticsDao.getProductData(storage_id, startTime, endTime, isFright, isUnit);
    }
}
