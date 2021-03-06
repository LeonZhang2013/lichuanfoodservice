package com.lichuan.sale.service.wx;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lichuan.sale.configurer.XCXInfo;
import com.lichuan.sale.core.CustomException;
import com.lichuan.sale.model.ChatRecord;
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
import java.util.List;
import java.util.Map;

@Service
public class WxService extends BaseService {

    public String getXcxId(String code) throws Exception {
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("appid", XCXInfo.APPID));
        params.add(new BasicNameValuePair("secret", XCXInfo.SECRET));
        params.add(new BasicNameValuePair("js_code", code));
        params.add(new BasicNameValuePair("grant_type", XCXInfo.AUTHORIZATION_CODE));
        String content = NetUtils.post(params, XCXInfo.URL_OPENID);
        // 如果请求成功
        if (content == null) throw new Exception("获取openId失败");
        JSONObject jsonObject = (JSONObject) JSON.parse(content);
//        map.put("openid", jsonObject.getString("openid"));
//        map.put("session_key", jsonObject.getString("session_key"));
        return jsonObject.getString("openid");
    }

    public User getUserByCode(String xcxId) throws Exception {
        User user = userDao.getUserByXcxId(xcxId);
        if (user != null && StringUtils.isNull(user.getToken())) {
            String token = StringUtils.uuid();
            userDao.updateToken(user.getId(), token);
            user.setToken(token);
        }
        return user;
    }


    public List<Map<String, Object>> getSaler(String storageId) throws CustomException {
        return userDao.getProxyByStorageId(storageId);
    }

    @Transactional
    public void addUser(User user, UserAddress userAddress) throws Exception {
        user.setRealname(userAddress.getName());
        user.setUsername(user.getMobile());
        Long userId = Tools.generatorId();
        Long addressId = Tools.generatorId();
        user.setId(userId);
        userAddress.setUser_id(userId);
        userAddress.setId(addressId);
        user.setAddress_id(addressId);
        userDao.addUser(user);
        userDao.addAddress(userAddress);
    }

    public void addUserAddress(UserAddress userAddress) {
        Long id = Tools.generatorId();
        userAddress.setId(id);
        userDao.addAddress(userAddress);
    }

    public User login(String phone) throws Exception {
        User user = userDao.getUserByPhone(phone);
        if (user == null) throw new CustomException("用户不存在");
        if (user.getStatus_() == 2) throw new CustomException("帐户被冻结");
        String token = StringUtils.uuid();
        userDao.updateToken(user.getId(), token);
        user.setToken(token);
        return user;
    }

    public List<Map<String, Object>> getClientChat(String user_id) throws CustomException {
        return userDao.getClientChat(user_id);
    }

    public Boolean setClientChat(ChatRecord record) {
        return userDao.setClientChat(record) > 0;
    }
}
