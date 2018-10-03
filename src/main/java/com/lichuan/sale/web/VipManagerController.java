package com.lichuan.sale.web;


import com.lichuan.sale.core.CustomException;
import com.lichuan.sale.model.Vip;
import com.lichuan.sale.result.Code;
import com.lichuan.sale.result.MultiResult;
import com.lichuan.sale.result.SingleResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("vip")
public class VipManagerController extends BaseController {


    @RequestMapping("getVipList")
    public MultiResult<Map<String, Object>> getVip() {
        MultiResult<Map<String, Object>> result = new MultiResult<>();
        try {
            List<Map<String, Object>> data = mVipService.getVip();
            result.setData(data);
        } catch (Exception e) {
            result.setMessage(e.getMessage());
            result.setCode(Code.ERROR);
        }
        return result;
    }

    @RequestMapping("addOrUpdateVip")
    public SingleResult<Object> addVip(Vip vip) {
        SingleResult<Object> result = new SingleResult();
        try {
            Boolean message;
            if (vip.getId() != null) {
                if (vip.getId() == 1) throw new CustomException("一级会员不可修改");
                message = mVipService.updateVip(vip);
            } else {
                message = mVipService.addVip(vip);
            }
            if (message) {
                result.setMessageOfSuccess();
            } else {
                result.setMessageOfError();
            }
        } catch (Exception e) {
            result.setMessage(e.getMessage());
            result.setCode(Code.ERROR);
        }
        return result;
    }

    @RequestMapping("deleteVip")
    public SingleResult<Object> delete(Long id) {
        SingleResult<Object> result = new SingleResult();
        try {
            Boolean message;

            if (id != null) {
                if (id == 1) throw new CustomException("一级会员不可修改");

                message = mVipService.deleteVip(id);
                if (message) {
                    result.setMessageOfSuccess();
                } else {
                    result.setMessageOfError();
                }
            }
        } catch (Exception e) {
            result.setMessage(e.getMessage());
            result.setCode(Code.ERROR);
        }
        return result;
    }

}
