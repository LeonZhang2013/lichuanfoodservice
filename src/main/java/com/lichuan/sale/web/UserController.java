package com.lichuan.sale.web;

import com.lichuan.sale.core.CustomException;
import com.lichuan.sale.model.User;
import com.lichuan.sale.model.UserAddress;
import com.lichuan.sale.result.Code;
import com.lichuan.sale.result.MultiResult;
import com.lichuan.sale.result.SingleResult;
import com.lichuan.sale.tools.StringUtils;
import com.lichuan.sale.tools.sqltools.Pager;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Created by leonZhang on 2018/05/14.
 */
@RestController
@RequestMapping("/user")
public class UserController extends BaseController {


    @PostMapping("login")
    public SingleResult<Object> login(String username, String password) {
        SingleResult<Object> result = new SingleResult<>();
        try {
            if (StringUtils.isNotBlank(username) && StringUtils.isNotBlank(password)) {
                Map<String, Object> login = userService.login(username, password);
                result.setCode(Code.SUCCESS);
                result.setData(login);
                result.setMessageOfSuccess("登陆成功");
            } else {
                result.setCode(Code.EXP_PARAM);
            }
        } catch (Exception e) {
            result.setMessageOfError(e.getMessage());
        }
        return result;
    }


    @PostMapping("register")
    public SingleResult<String> register(User user, UserAddress userAddress,String vercode) {
        SingleResult<String> result = new SingleResult<>();
        try {
            if (commonService.verCode(user.getMobile(), vercode)) {
                if (null == user.getRole_id()) user.setRole_id(9999l);
                user.setUsername(user.getMobile());
                userService.addUser(user, userAddress);
                result.setMessageOfSuccess("注册用户成功");
            }else {
                result.setCode(Code.ERROR);
                result.setMessage("验证码有误");
            }
        } catch (Exception e) {
            result.setMessageOfError(e.getMessage());
        }
        return result;
    }

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


    @PostMapping("getAddress")
    public SingleResult<Object> getAddress(String address_id) {
        SingleResult<Object> result = new SingleResult<>();
        try {
            Map<String, Object> address = userService.getAddress(address_id);
            result.setCode(Code.SUCCESS);
            result.setData(address);
        } catch (CustomException e) {
            result.setMessageOfError(e.getMessage());
        }
        return result;
    }


    @PostMapping("updateRole")
    public SingleResult<Object> updateRole(String user_id, Integer role_id) {
        SingleResult<Object> result = new SingleResult<Object>();
        try {
            boolean isOk = userService.updateRole(user_id, role_id);
            if (isOk) {
                result.setMessageOfSuccess("授权成功");
            }else {
                result.setMessageOfError("授权失败");
            }
        } catch (Exception e) {
            result.setMessageOfError(e.getMessage());
        }
        return result;
    }

    @PostMapping("updateParentId")
    public SingleResult<Object> updateProductPercent(String user_id, Long parent_id) {
        SingleResult<Object> result = new SingleResult<Object>();
        try {
            boolean isOk = userService.updateParentId(user_id, parent_id);
            if (isOk) {
                result.setMessageOfSuccess("修改成功");
            }else{
                result.setMessageOfError("修改失败");
            }
        } catch (Exception e) {
            result.setMessageOfError(e.getMessage());
        }
        return result;
    }

    @PostMapping("updateState")
    public SingleResult<Object> updateState(String user_id, String state) {
        SingleResult<Object> result = new SingleResult<Object>();
        try {
            boolean isOk = userService.updateState(user_id, state);
            if (isOk) {
                result.setMessageOfSuccess("修改成功");
            }else{
                result.setMessageOfError("修改失败");
            }
        } catch (Exception e) {
            result.setMessageOfError(e.getMessage());
        }
        return result;
    }

    @RequestMapping(value = "updateMultiLogin", method = RequestMethod.POST)
    public SingleResult<String> updateMultiLogin(Integer multi) {
        SingleResult<String> result = new SingleResult<>();
        result.setCode(Code.ERROR);
        try {
            boolean isOk = userService.updateMultiLogin(getUserId(), multi);
            if (isOk) result.setCode(Code.SUCCESS);
        } catch (Exception e) {
            result.setMessage(e.getMessage());
        }
        return result;
    }


    @RequestMapping(value = "updatePwd", method = RequestMethod.POST)
    public SingleResult<String> updatePwd(String newPwd, String vercode, String mobile) {
        SingleResult<String> result = new SingleResult<>();
        try {
            if (commonService.verCode(mobile, vercode)) {
                boolean b = userService.updatePwd(mobile, newPwd);
                if(b) throw new CustomException("修改密码出错");
                result.setMessageOfSuccess("修改成功");
            } else {
                result.setMessageOfError("验证码有误");
            }
        } catch (Exception e) {
            result.setMessageOfError(e.getMessage());
        }
        return result;
    }


    @RequestMapping(value = "updateAddress", method = RequestMethod.POST)
    public SingleResult<Object> updateAddress(String user_id, String address) {
        SingleResult<Object> result = new SingleResult<Object>();
        try {
            boolean b = userService.updateAddress(user_id, address);
        } catch (Exception e) {
            result.setMessageOfError(e.getMessage());
        }
        return result;
    }

    @RequestMapping(value = "updateUserCity", method = RequestMethod.POST)
    public SingleResult<Object> updateUserCity(String user_id, String city) {
        SingleResult<Object> result = new SingleResult<Object>();
        try {
            boolean isOk = userService.updateUserCity(user_id, city);
            if (isOk) {
                result.setMessageOfSuccess("修改成功");
            }else{
                result.setMessageOfError("修改失败");
            }
        } catch (Exception e) {
            result.setMessageOfError(e.getMessage());
        }
        return result;
    }

}
