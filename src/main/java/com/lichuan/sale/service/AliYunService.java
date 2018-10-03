package com.lichuan.sale.service;

import com.lichuan.sale.configurer.AliYunSMS;
import com.lichuan.sale.configurer.LCConfig;
import com.lichuan.sale.result.Code;
import com.lichuan.sale.result.SingleResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import java.util.Random;


@Service
public class AliYunService extends BaseService {

    @Autowired
    AliYunSMS aliYunSMS;

    @Autowired
    protected LCConfig zsConfig;

    public SingleResult<Map<String, Object>> sendMassage(String phone) {
        //1、生成随机吗
        String verCode = getVerCodeNum(zsConfig.getVarCodelength());
        Date expirseDate = getExpiresIn(zsConfig.getVarExpiresIn());

        SingleResult<Map<String, Object>> result = new SingleResult<Map<String, Object>>();
        try {
            verCodeDao.saveCode(phone, verCode, expirseDate);
            String message = aliYunSMS.sendSms(phone, verCode).getMessage();
            result.setCode(Code.SUCCESS);
            result.setMessage("OK".equalsIgnoreCase(message) ? "发送成功" : message);
        } catch (Exception e) {
            result.setCode(Code.ERROR);
            result.setMessage(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    private Date getExpiresIn(long varExpiresIn) {
        long timeStamp = (System.currentTimeMillis() + varExpiresIn * 1000);
        return new Date(timeStamp);
    }

    private String getVerCodeNum(int varCodelength) {
        int verCode = 0;
        if (varCodelength == 6) {
            verCode = 1000000 - new Random().nextInt(899999);
        } else {
            verCode = 10000 - new Random().nextInt(8999);
        }
        return String.valueOf(verCode);
    }

}
