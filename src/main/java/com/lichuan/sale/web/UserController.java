package com.lichuan.sale.web;

import com.lichuan.sale.core.CustomException;
import com.lichuan.sale.result.Code;
import com.lichuan.sale.result.MapResult;
import com.lichuan.sale.result.MultiResult;
import com.lichuan.sale.result.SingleResult;
import com.lichuan.sale.tools.Arith;
import com.lichuan.sale.tools.sqltools.Pager;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Created by leonZhang on 2018/05/14.
 */
@RestController
@RequestMapping("user")
public class UserController extends BaseController {


    @RequestMapping("getUserList")
    public MultiResult<Map<String, Object>> getUserList(Pager<Map<String, Object>> pager, Long role_id, String key, Integer enable) {
        MultiResult<Map<String, Object>> result = new MultiResult<>();
        try {
            result = userService.getUserList(pager, getUser(), role_id, key, enable);
        } catch (Exception e) {
            result.setMessage(e.getMessage());
            result.setCode(Code.ERROR);
        }
        return result;
    }


    @RequestMapping(value = "getUserListByProxy", method = RequestMethod.GET)
    public SingleResult<List<Map<String, Object>>> getUserListByProxy(long proxy_id) {
        SingleResult<List<Map<String, Object>>> result = new SingleResult<>();
        try {
            List<Map<String, Object>> users = userService.getUserListByProxy(proxy_id);
            result.setMessageOfSuccess();
            result.setData(users);
        } catch (Exception e) {
            result.setMessage(e.getMessage());
        }
        return result;
    }


    @PostMapping("getMyAddressAndFreight")
    public MapResult getMyFright() {
        MapResult result = new MapResult();
        try {
            Map<String, Object> addressAndDistance = userService.addressAndDistance(getUserId());
            List<Map<String, Object>> wxShopCartList = shopCartService.getWxShopCartList(getUserId());
            Map<String, Object> charge = Arith.calculationCharge(addressAndDistance.get("distance").toString(), wxShopCartList, getUser().getVip());
            result.setCode(Code.SUCCESS);
            result.put("address", addressAndDistance);
            result.put("cart", wxShopCartList);
            result.put("charge", charge);
        } catch (CustomException e) {
            result.setMessageOfError(e.getMessage());
        }
        return result;
    }


    @RequestMapping(value = "updatePwd", method = RequestMethod.POST)
    public SingleResult<String> updatePwd(String newPwd, String vercode, String mobile) {
        SingleResult<String> result = new SingleResult<>();
        try {
            if (commonService.verCode(mobile, vercode)) {
                userService.updatePwd(mobile, newPwd);
                result.setMessageOfSuccess("修改成功");
            } else {
                result.setMessageOfError("验证码有误");
            }
        } catch (Exception e) {
            result.setMessageOfError(e.getMessage());
        }
        return result;
    }


    @GetMapping("getUserAddress")
    public SingleResult<Object> getUserAddress() {
        SingleResult<Object> result = new SingleResult<>();
        try {
            List<Map<String, Object>> address = userService.getUserAddress(getUserId());
            result.setCode(Code.SUCCESS);
            result.setData(address);
        } catch (Exception e) {
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @RequestMapping(value = "updateAddress", method = RequestMethod.POST)
    public SingleResult<Object> updateAddress(String user_id, String address) {
        SingleResult<Object> result = new SingleResult<Object>();
        try {
            userService.updateAddress(user_id, address);
            result.setMessageOfSuccess("修改成功");
        } catch (Exception e) {
            result.setMessageOfError(e.getMessage());
        }
        return result;
    }


    @PostMapping("deleteAddress")
    public SingleResult<Object> deleteAddress(String address_id) {
        SingleResult<Object> result = new SingleResult<Object>();
        try {
            userService.deleteAddress(address_id);
            result.setMessageOfSuccess("删除成功");
        } catch (Exception e) {
            result.setMessageOfError(e.getMessage());
        }
        return result;
    }

    @PostMapping("setDefaultAddress")
    public SingleResult<String> setDefaultAddress(String address_id) {
        SingleResult<String> result = new SingleResult();
        try {
            userService.setDefaultAddress(address_id);
            result.setMessageOfSuccess("设置成功");
            result.setData(address_id);
        } catch (Exception e) {
            result.setMessageOfError(e.getMessage());
        }
        return result;
    }


    @PostMapping("test")
    public SingleResult<String> test(String address_id) {
        SingleResult<String> result = new SingleResult();
        System.out.println(address_id);
        return result;
    }


}
