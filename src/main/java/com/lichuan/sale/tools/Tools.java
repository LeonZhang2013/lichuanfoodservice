package com.lichuan.sale.tools;


import com.lichuan.sale.core.CustomException;
import com.lichuan.sale.tools.encrypt.Algorithm;
import com.lichuan.sale.tools.encrypt.MessageDigestUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Tools {

    public static Long generatorId() {
        return System.currentTimeMillis()+new Random().nextInt(999);
    }

    public static String encryptPass(String username,String newPwd) throws CustomException {
        try {
            return MessageDigestUtils.encrypt(username + newPwd, Algorithm.SHA1);
        } catch (Exception e) {
            throw new CustomException("加密失败");
        }
    }

    public static void PraseDate(List<Map<String, Object>> data, String key) {
        try {
            for (int i=0; i<data.size(); i++){
                Date o = (Date)data.get(i).get(key);
                if (o!=null){
                    String s = DateUtils.formatTime(o.getTime());
                    data.get(i).put(key,s);
                }
            }
        }catch (Exception e){

        }
    }
}