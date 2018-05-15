package com.lichuan.sale.service;

import com.alibaba.fastjson.JSONArray;
import com.lichuan.sale.core.CustomException;
import com.lichuan.sale.model.Role;
import com.lichuan.sale.model.Version;
import com.lichuan.sale.result.Code;
import com.lichuan.sale.result.MultiResult;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CommonService extends BaseService {


    public boolean verCode(String phone, String verCode) throws CustomException {
        boolean isOk = false;
        List<String> codes = verCodeDao.getVerCode(phone);//查询未过期的验证码
        if (codes != null && codes.size() > 0) {
            for (String vc : codes) {//可能发送多条验证码。
                if (vc.equals(verCode)) {//匹配验证码
                    isOk = true;
                    int deleteInt = verCodeDao.delCodeByPhone(phone);
                    break;
                }
            }
        }
        return isOk;
    }

    /**
     * 获得省市区
     * @param parentId
     * @return
     * @throws Exception
     */
    public List<Map<String,Object>> getArea(Long parentId)throws Exception{
        return commonDao.getArea(parentId);
    }

    /**
     * 查询这 角色id
     * @param role_id 获取的角色列表不能大于自己的角色。
     * @return
     */
    public List<Role> getRoles(Long role_id) {
        return commonDao.getRoles(role_id);
    }

    /**
     * 检查更新
     * @param code
     * @param packageName
     * @return
     * @throws Exception
     */
    public Version checkVersion(Integer code, String packageName)throws Exception{
        return commonDao.checkVersion(code,packageName);
    }
}
