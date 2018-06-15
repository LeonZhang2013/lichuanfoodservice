package com.lichuan.sale.wx;


import com.lichuan.sale.core.CustomException;
import com.lichuan.sale.model.User;
import com.lichuan.sale.model.UserAddress;
import com.lichuan.sale.result.Code;
import com.lichuan.sale.result.MapResult;
import com.lichuan.sale.result.MultiResult;
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
    public MapResult login(String code) {
        MapResult result = new MapResult();
        try {
            String xcxId = wxService.getXcxId(code);
            User user = wxService.getUserByCode(xcxId);
            if (user != null) {
                List<Map<String, Object>> cart = shopCartService.getWxShopCartList(user.getId());
                result.setMessageOfSuccess("登陆成功");
                result.put("user", user);
                result.put("cart", cart);
            }else{
                result.setCode(Code.NO_USER);
                result.put("xcx_id", xcxId);
            }
        } catch (Exception e) {
            result.setMessageOfError(e.getMessage());
        }
        return result;
    }


    @PostMapping("addWXAddress")
    public SingleResult<Object> addWXAddress(User user, UserAddress userAddress) {

        SingleResult<Object> result = new SingleResult();
        try {
            if (user.getXcx_id() == null && user.getXcx_id().length() < 1) {
                throw new CustomException("小程序id 不正确");
            }
            wxService.addUser(user, userAddress);
            user = wxService.getUserByCode(user.getXcx_id());
            result.setMessageOfSuccess();
            result.setData(user);
        } catch (Exception e) {
            result.setMessageOfError(e.getMessage());
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

}
