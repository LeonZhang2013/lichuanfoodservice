package com.lichuan.sale.service;

import com.lichuan.sale.core.CustomException;
import com.lichuan.sale.model.Permission;
import com.lichuan.sale.model.Role;
import com.lichuan.sale.model.User;
import com.lichuan.sale.model.UserAddress;
import com.lichuan.sale.result.Code;
import com.lichuan.sale.result.MultiResult;
import com.lichuan.sale.result.SingleResult;
import com.lichuan.sale.tools.StringUtils;
import com.lichuan.sale.tools.Tools;
import com.lichuan.sale.tools.sqltools.MySql;
import com.lichuan.sale.tools.sqltools.Pager;
import com.lichuan.sale.tools.sqltools.SQLTools;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by leonZhang on 2018/05/14.
 */
@Service
public class UserService extends BaseService {

    @Transactional
    public Map<String, Object> login(String username, String password) throws CustomException {
        Map<String, Object> data = new HashMap<>();
        try {
            User user = userDao.login(username, password);
            if (user == null) throw new CustomException("用户名或密码错误");
            List<Role> role = commonDao.getRoles(user.getRole_id());
            if (user.getRole_id() == 0) {
                throw new CustomException("正在审核中...");
            }
            //是否允许多用户登录
            if (StringUtils.isNull(user.getToken()) || user.getIs_multi() == 0) {
                String token = StringUtils.uuid();
                userDao.updateToken(user.getId(), token);
                user.setToken(token);
            }
            List<Map<String, Object>> permissionList = userDao.getPermissionByRoleId(user.getRole_id());
            Map<String, Object> notice = commonDao.getNotice();

            data.put("permission", permissionList);
            data.put("user", user);
            data.put("roleList", role);
            data.put("notice", notice);
        } catch (Exception e) {
            throw new CustomException(e.getMessage());
        }
        return data;
    }

    public void addUser(User user, UserAddress userAddress) throws CustomException {
        String encryptPass = Tools.encryptPass(user.getUsername(), user.getPassword());
        user.setPassword(encryptPass);
        if (userDao.userExist(user.getUsername())) {
            throw new CustomException("用户名或手机号已存在");
        }

        Long id = userDao.addUser(user);
        userAddress.setUser_id(id);
        userDao.addAddress(userAddress);
    }

    public Map<String, Object> getAddress(String address_id) throws CustomException {
        return userDao.getAddress(address_id);
    }

    public List<Map<String, Object>> getPermissionByRoleId(String user_id) {
        return userDao.getPermissionByRoleId(Long.parseLong(user_id));
    }

    public MultiResult<Map<String, Object>> getUserList(
            Pager<Map<String, Object>> pager, User user, Long role_id, String key, Integer enable) {

        MySql sql = new MySql("select u.*, r.name role_name,a.city,a.address,pu.realname ");
        sql.append(" from user u, user pu,role r,user_address a ");
        sql.append("where u.id= pu.parent_id and u.role_id = r.id and u.address_id = a.id ");
        sql.append("and r.level > (select level from role where id = ?) and u.role_id = ?");
        sql.addValue(user.getRole_id());
        sql.addValue(role_id);
        sql.notNullAppend(" and u.enable = ?", enable);
        key = SQLTools.FuzzyKey(key);
        sql.notNullAppend(" and (u.mobile like ? or a.address like ? or u.nickname like ? or a.city like ? or u.realname like ?)",
                key, key, key, key, key);
        sql.orderBy("u.enable asc", "u.id desc");
        //long count = sql.getCount(jdbcTemplate);
        sql.limit(pager);
        MultiResult<Map<String, Object>> result = new MultiResult<>();
        List<Map<String, Object>> data = jdbcTemplate.queryForList(sql.toString(), sql.getValues());
        if (null != data && data.size() > 0) {
            SQLTools.removePass(data);
            result.setCode(Code.SUCCESS);
            result.setData(data);
        } else {
            result.setCode(Code.NO_DATA);
        }
        //result.setTotal(count);
        return result;

    }

    public boolean updateRole(String user_id, Integer role_id) throws CustomException {
        int effect = userDao.updateUserRole(user_id, role_id);
        return effect > 0;
    }

    public boolean updateParentId(String user_id, Long parent_id) {
        int effect = userDao.updateParentId(user_id, parent_id);
        return effect > 0;
    }

    public boolean updateState(String user_id, String state) {
        int effect = userDao.updateUserState(user_id, state);
        return effect > 0;
    }

    public boolean updateMultiLogin(Long userId, Integer multi) {
        int effect = userDao.updateMultiLogin(userId, multi);
        return effect > 0;
    }

    @Transactional
    public boolean updatePwd(String mobile, String newPwd) throws Exception {
        if (!StringUtils.isNotBlank(newPwd)) throw new CustomException("密码不能为空");
        int effect = userDao.updateUserPwd(mobile, newPwd);
        return effect > 0;
    }

    public void phoneHasEixst(String phone) throws CustomException {
        boolean b = userDao.userExist(phone);
        if (b) {
            throw new CustomException("手机号已存在");
        }
    }

    public boolean updateAddress(String user_id,String address) {
        int effect = userDao.updateUserAddress(user_id,address);
        return effect > 0;
    }

    public boolean updateUserCity(String user_id,String city) {
        int effect = userDao.updateUserCity(user_id,city);
        return effect > 0;
    }
}
