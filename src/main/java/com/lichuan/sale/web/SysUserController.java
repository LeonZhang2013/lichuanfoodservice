package com.lichuan.sale.web;

import com.lichuan.sale.configurer.RoleConstant;
import com.lichuan.sale.core.CustomException;
import com.lichuan.sale.model.User;
import com.lichuan.sale.result.Code;
import com.lichuan.sale.result.MultiResult;
import com.lichuan.sale.result.SingleResult;
import com.lichuan.sale.tools.StringUtils;
import com.lichuan.sale.tools.sqltools.Pager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * Created by leonZhang on 2018/05/14.
 */
@RestController
@RequestMapping("sys_user")
public class SysUserController extends BaseController {


    @PostMapping("login")
    public SingleResult<Object> login(String username, String password) {
        SingleResult<Object> result = new SingleResult<>();
        try {
            if (StringUtils.isNotBlank(username) && StringUtils.isNotBlank(password)) {
                Map<String, Object> login = sysUserService.login(username, password);
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
    public SingleResult<String> register(User user, String vercode) {
        SingleResult<String> result = new SingleResult<>();
        try {
            if (commonService.verCode(user.getMobile(), vercode)) {
                if (null == user.getRole_id()) user.setRole_id(RoleConstant.VERIFY_ID);
                user.setUsername(user.getMobile());
                sysUserService.addUser(user);
                result.setMessageOfSuccess("注册用户成功");
            } else {
                result.setCode(Code.ERROR);
                result.setMessage("验证码有误");
            }
        } catch (Exception e) {
            result.setMessageOfError(e.getMessage());
        }
        return result;
    }

    @RequestMapping("getSales")
    public MultiResult<Map<String, Object>> getSales() {
        MultiResult<Map<String, Object>> result = new MultiResult<>();
        try {
            List<Map<String, Object>> userList = sysUserService.getSales();
            result.setMessageOfSuccess();
            result.setData(userList);
        } catch (Exception e) {
            result.setMessage(e.getMessage());
            result.setCode(Code.ERROR);
        }
        return result;
    }

    @RequestMapping("getUserList")
    public MultiResult<Map<String, Object>> getUserList(Pager<Map<String, Object>> pager, Long role_id, String key, Integer enable) {
        MultiResult<Map<String, Object>> result = new MultiResult<>();
        try {
            List<Map<String, Object>> userList = sysUserService.getUserList(pager, getUserId(), role_id, key, enable);
            result.setMessageOfSuccess();
            result.setData(userList);
        } catch (Exception e) {
            result.setMessage(e.getMessage());
            result.setCode(Code.ERROR);
        }
        return result;
    }


    @PostMapping("updateRole")
    public SingleResult<Object> updateRole(String user_id, Integer role_id) {
        SingleResult<Object> result = new SingleResult<Object>();
        try {
            boolean isOk = sysUserService.updateRole(user_id, role_id);
            if (isOk) {
                result.setMessageOfSuccess("授权成功");
            } else {
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
            boolean isOk = sysUserService.updateParentId(user_id, parent_id);
            if (isOk) {
                result.setMessageOfSuccess("修改成功");
            } else {
                result.setMessageOfError("修改失败");
            }
        } catch (Exception e) {
            result.setMessageOfError(e.getMessage());
        }
        return result;
    }

    @PostMapping("updateStatus")
    public SingleResult<Object> updateStatus(String user_id, String status) {
        SingleResult<Object> result = new SingleResult<Object>();
        try {
            if (status == null) throw new CustomException("参数为空");
            sysUserService.updateStatus(user_id, status);
            result.setMessageOfSuccess("修改成功");
        } catch (Exception e) {
            result.setMessageOfError(e.getMessage());
        }
        return result;
    }

    @RequestMapping(value = "updateMultiLogin", method = RequestMethod.POST)
    public SingleResult<String> updateMultiLogin(Integer multi) {
        SingleResult<String> result = new SingleResult<>();
        try {
            if (multi == null || multi > 2) throw new CustomException("参数异常");
            sysUserService.updateMultiLogin(getUserId(), multi);
            result.setCode(Code.SUCCESS);
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
                sysUserService.updatePwd(mobile, newPwd);
                result.setMessageOfSuccess("修改成功");
            } else {
                result.setMessageOfError("验证码有误");
            }
        } catch (Exception e) {
            result.setMessageOfError(e.getMessage());
        }
        return result;
    }


}
