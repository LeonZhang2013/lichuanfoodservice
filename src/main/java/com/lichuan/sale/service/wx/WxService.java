package com.lichuan.sale.service.wx;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lichuan.sale.configurer.RoleConstant;
import com.lichuan.sale.configurer.XCXInfo;
import com.lichuan.sale.model.User;
import com.lichuan.sale.service.BaseService;
import com.lichuan.sale.tools.NetUtils;
import com.lichuan.sale.tools.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class WxService extends BaseService {

    public User getXcxId(String code) throws Exception {
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("appid", XCXInfo.getAPPID()));
        params.add(new BasicNameValuePair("secret", XCXInfo.getSECRET()));
        params.add(new BasicNameValuePair("js_code", code));
        params.add(new BasicNameValuePair("grant_type", XCXInfo.AUTHORIZATION_CODE));

        String content = NetUtils.post(params, XCXInfo.URL_OPENID);
        // 如果请求成功
        Map<String, String> map;
        if (content == null) throw new Exception("获取openId失败");

        map = new HashMap<>();
        JSONObject jsonObject = (JSONObject) JSON.parse(content);
        map.put("openid", jsonObject.getString("openid"));
        map.put("session_key", jsonObject.getString("session_key"));
        User user = userDao.getUserByXcxId(jsonObject.getString("openid"));


        if (user == null) {
            throw new Exception("该账户尚未登记注册");
        } else if (StringUtils.isNull(user.getToken())) {
            String token = StringUtils.uuid();
            userDao.updateToken(user.getId(), token);
            user.setToken(token);

        }
        return user;
    }
}
