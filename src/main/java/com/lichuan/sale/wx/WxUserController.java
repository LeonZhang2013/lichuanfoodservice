package com.lichuan.sale.wx;


import com.lichuan.sale.model.User;
import com.lichuan.sale.result.Code;
import com.lichuan.sale.result.SingleResult;
import com.lichuan.sale.tools.StringUtils;
import com.lichuan.sale.web.BaseController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("wx/user")
public class WxUserController extends BaseController {

    @PostMapping("login")
    public SingleResult<Object> login(String code) {
        SingleResult<Object> result = new SingleResult<>();
        try {
            if (StringUtils.isNotBlank(code) && StringUtils.isNotBlank(code)) {
                User user = wxService.getXcxId(code);
                result.setMessageOfSuccess("登陆成功");
                result.setData(user);
            } else {
                result.setCode(Code.EXP_PARAM);
            }
        } catch (Exception e) {
            result.setMessageOfError(e.getMessage());
        }
        return result;
    }

}
