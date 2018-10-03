package com.lichuan.sale.wx;


import com.lichuan.sale.core.CustomException;
import com.lichuan.sale.model.ChatRecord;
import com.lichuan.sale.model.User;
import com.lichuan.sale.model.UserAddress;
import com.lichuan.sale.model.Vip;
import com.lichuan.sale.result.Code;
import com.lichuan.sale.result.MapResult;
import com.lichuan.sale.result.SingleResult;
import com.lichuan.sale.tools.StringUtils;
import com.lichuan.sale.web.BaseController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("wx/user")
public class WxUserController extends BaseController {

    @PostMapping("login")
    public MapResult login(String phone, String verCode) {
        MapResult result = new MapResult();
        try {
            if (!commonService.verCode(phone, verCode)) {
                throw new CustomException("验证码有误");
            }
            User user = wxService.login(phone);
            if (user != null) {
                List<Map<String, Object>> cart = shopCartService.getWxShopCartList(user.getId());
                result.setMessageOfSuccess("登陆成功");
                Vip vip = mVipService.getUserVipInfo(user.getId());
                user.setVip(vip);
                result.put("user", user);
                result.put("cart", cart);
                result.put("vip", vip);
            } else {
                result.setCode(Code.NO_USER);
            }
        } catch (Exception e) {
            result.setMessageOfError(e.getMessage());
        }
        return result;
    }


    @GetMapping("getOpenId")
    public SingleResult<Object> getOpenId(String code) {
        SingleResult<Object> result = new SingleResult<>();
        try {
            String xcxId = wxService.getXcxId(code);
            result.setData(xcxId);
        } catch (Exception e) {
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @PostMapping("start")
    public MapResult start() {
        MapResult result = new MapResult();
        try {
            List<Map<String, Object>> cart = shopCartService.getWxShopCartList(getUserId());
            result.setMessageOfSuccess("登陆成功");
            result.put("user", getUser());
            result.put("cart", cart);
            result.put("vip", mVipService.getUserVipInfo(getUser().getId()));
        } catch (Exception e) {
            result.setMessageOfError(e.getMessage());
        }
        return result;
    }


    @PostMapping("register")
    public SingleResult<Object> register(User user, UserAddress userAddress) {
        SingleResult<Object> result = new SingleResult();
        try {
            userService.phoneHasEixst(user.getMobile());
            user.setStorage_id(null);//用户库房id放在地址里面
            wxService.addUser(user, userAddress);
            user = wxService.login(user.getMobile());
            result.setMessageOfSuccess();
            result.setData(user);
        } catch (Exception e) {
            result.setMessageOfError(e.getMessage());
        }
        return result;
    }

    @PostMapping("addWXAddress")
    public SingleResult<Object> addWXAddress(UserAddress userAddress) {

        SingleResult<Object> result = new SingleResult();
        try {
            userAddress.setUser_id(getUserId());
            wxService.addUserAddress(userAddress);
            result.setMessageOfSuccess();
        } catch (Exception e) {
            String message = e.getMessage();
            message = message.substring(message.indexOf("Duplicate entry"), message.length() - 1);
            if (message.contains("mobile")) {
                result.setMessageOfError("手机号已存在");
            } else {
                result.setMessageOfError(e.getMessage());
            }
        }
        return result;
    }


    @GetMapping("getSaler")
    public SingleResult<Object> getSaler(String storageId) {
        SingleResult<Object> result = new SingleResult();
        try {
            if (StringUtils.isNotBlank(storageId) && StringUtils.isNotBlank(storageId)) {
                List<Map<String, Object>> data = wxService.getSaler(storageId);
                result.setMessageOfSuccess();
                result.setData(data);
            } else {
                result.setCode(Code.EXP_PARAM);
            }
        } catch (Exception e) {
            result.setMessageOfError(e.getMessage());
        }
        return result;
    }

    @GetMapping("getClientChat")
    public SingleResult<Object> getClientChat(String user_id) {
        SingleResult<Object> result = new SingleResult();
        try {
            user_id = "1533350388711";
            List<Map<String, Object>> data = wxService.getClientChat(user_id);
            result.setMessageOfSuccess();
            result.setData(data);
        } catch (Exception e) {
            result.setMessageOfError(e.getMessage());
        }
        return result;
    }

    @GetMapping("setClientChat")
    public SingleResult<Object> setClientChat(ChatRecord record) {
        SingleResult<Object> result = new SingleResult();
        try {
            Boolean isOk = wxService.setClientChat(record);
            if (isOk) {
                result.setMessageOfSuccess();
            }
        } catch (Exception e) {
            result.setMessageOfError(e.getMessage());
        }
        return result;
    }

}
