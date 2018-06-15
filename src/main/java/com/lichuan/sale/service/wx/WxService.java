package com.lichuan.sale.service.wx;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lichuan.sale.configurer.RoleConstant;
import com.lichuan.sale.configurer.XCXInfo;
import com.lichuan.sale.core.CustomException;
import com.lichuan.sale.model.User;
import com.lichuan.sale.model.UserAddress;
import com.lichuan.sale.service.BaseService;
import com.lichuan.sale.tools.NetUtils;
import com.lichuan.sale.tools.StringUtils;
import com.lichuan.sale.tools.Tools;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class WxService extends BaseService {

    public String getXcxId(String code) throws Exception {
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("appid", XCXInfo.getAPPID()));
        params.add(new BasicNameValuePair("secret", XCXInfo.getSECRET()));
        params.add(new BasicNameValuePair("js_code", code));
        params.add(new BasicNameValuePair("grant_type", XCXInfo.AUTHORIZATION_CODE));
        String content = NetUtils.post(params, XCXInfo.URL_OPENID);
        // 如果请求成功
        if (content == null) throw new Exception("获取openId失败");
        JSONObject jsonObject = (JSONObject) JSON.parse(content);
//        map.put("openid", jsonObject.getString("openid"));
//        map.put("session_key", jsonObject.getString("session_key"));
        return  jsonObject.getString("openid");
    }

    public User getUserByCode(String xcxId) throws Exception {
        User user = userDao.getUserByXcxId(xcxId);
        if (user!=null&&StringUtils.isNull(user.getToken())) {
            String token = StringUtils.uuid();
            userDao.updateToken(user.getId(), token);
            user.setToken(token);
        }
        return user;
    }



    public List<Map<String,Object>> getSaler(String storageId) throws CustomException {
       return userDao.getProxyByStorageId(storageId);
    }

    @Transactional
    public void addUser(User user, UserAddress userAddress) throws Exception {
        userAddress.setName(user.getRealname());
        user.setUsername(user.getMobile());
        Long id = Tools.generatorId();
        user.setId(id);
        userAddress.setUser_id(id);
        userDao.addUser(user);
        userDao.addAddress(userAddress);
    }
}
