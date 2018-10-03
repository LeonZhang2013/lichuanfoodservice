package com.lichuan.sale.service;

import com.alibaba.fastjson.JSONArray;
import com.lichuan.sale.configurer.RoleConstant;
import com.lichuan.sale.core.CustomException;
import com.lichuan.sale.model.Role;
import com.lichuan.sale.model.Version;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CommonService extends BaseService {


    /**
     * 严重code
     *
     * @param phone
     * @param verCode
     * @return
     * @throws CustomException
     */
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
     *
     * @param parentId
     * @return
     * @throws Exception
     */
    public List<Map<String, Object>> getArea(Long parentId) throws Exception {
        return commonDao.getArea(parentId);
    }

    /**
     * 查询这 角色id
     *
     * @param role_id 获取的角色列表不能大于自己的角色。
     * @return
     */
    public List<Role> getRoles(Long role_id) {
        return commonDao.getRoles(role_id);
    }

    /**
     * 检查更新
     *
     * @param code
     * @param packageName
     * @return
     * @throws Exception
     */
    public Version checkVersion(Integer code, String packageName) throws Exception {
        return commonDao.checkVersion(code, packageName);
    }

    public List<Map<String, Object>> getPermissions() {
        return commonDao.getPermissions();
    }

    public void updatePermissions(Long userId, String role_id, JSONArray groupJson) {
        commonDao.updatePermissions(userId, role_id, groupJson);
    }

    public List<Map<String, Object>> getMyPermissions(String role_id) {
        return commonDao.getMyPermissions(role_id);
    }

    public Map<String, Object> getNoticeOfwx() {
        return commonDao.getNoticeOfClient();
    }
}
