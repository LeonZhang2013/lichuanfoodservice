package com.lichuan.sale.web;


import com.lichuan.sale.result.Code;
import com.lichuan.sale.result.SingleResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("statistic")
public class StatisticsController extends BaseController {


    /**
     * @param id          例： 2018
     * @param proxy_id
     * @param category_id
     * @param isFright    需要计算运费 传true   否者不传。
     * @param isUnit      件数传 true          统计货币不传。
     * @return
     */
    @GetMapping("getTimeData")
    public SingleResult<Object> getTimeData(int level, String id, String proxy_id, String category_id, boolean isFright, boolean isUnit) {
        SingleResult<Object> result = new SingleResult<>();
        try {
            List<Map<String, Object>> data = statisticsService.getTimeData(level, id, proxy_id, category_id, isFright, isUnit);
            result.setCode(Code.SUCCESS);
            result.setData(data);
        } catch (Exception e) {
            result.setMessageOfError(e.getMessage());
        }
        return result;
    }

    @GetMapping("getBusinessData")
    public SingleResult<Object> getBusinessData(int storageOrProxy, String id, String category_id, String startTime, String endTime, boolean isFright, boolean isUnit) {
        SingleResult<Object> result = new SingleResult<>();
        try {
            List<Map<String, Object>> data = statisticsService.getBusinessData(storageOrProxy, id, category_id, startTime, endTime, isFright, isUnit);
            result.setCode(Code.SUCCESS);
            result.setData(data);
        } catch (Exception e) {
            result.setMessageOfError(e.getMessage());
        }
        return result;
    }

    @GetMapping("getProductData")
    public SingleResult<Object> getProductData(String storage_id, String startTime, String endTime, boolean isFright, boolean isUnit) {
        SingleResult<Object> result = new SingleResult<>();
        try {
            List<Map<String, Object>> data = statisticsService.getProductData(storage_id, startTime, endTime, isFright, isUnit);
            result.setCode(Code.SUCCESS);
            result.setData(data);
        } catch (Exception e) {
            result.setMessageOfError(e.getMessage());
        }
        return result;
    }
}
