package com.lichuan.sale.service;

import com.lichuan.sale.annoation.MyRedis;
import com.lichuan.sale.configurer.RoleConstant;
import com.lichuan.sale.core.CustomException;
import com.lichuan.sale.model.Role;
import com.lichuan.sale.model.User;
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
public class SysUserService extends BaseService {

    @Transactional
    public Map<String, Object> login(String username, String password) throws CustomException {
        Map<String, Object> data = new HashMap<>();
        try {
            User user = sysUserDao.sysLogin(username, password);
            if (user == null) throw new CustomException("用户名或密码错误");
            if (user.getRole_id() == 0) throw new CustomException("正在审核中...");
            List<Role> role = commonDao.getRoles(user.getRole_id());

            //是否允许多用户登录
            if (StringUtils.isNull(user.getToken()) || user.getIs_multi() == 0) {
                String token = StringUtils.uuid();
                sysUserDao.updateToken(user.getId(), token);
                user.setToken(token);
            }
            List<Map<String, Object>> permissionList = sysUserDao.getPermissionByRoleId(user.getRole_id());
            Map<String, Object> notice = commonDao.getNoticeOfService();

            data.put("permission", permissionList);
            data.put("user", user);
            data.put("roleList", role);
            data.put("notice", notice);
        } catch (Exception e) {
            throw new CustomException(e.getMessage());
        }
        return data;
    }

    public void addUser(User user) throws CustomException {
        if (sysUserDao.userExist(user.getUsername())) {
            throw new CustomException("用户名或手机号已存在");
        }

        String encryptPass = Tools.encryptPass(user.getUsername(), user.getPassword());
        user.setPassword(encryptPass);
        Long id = Tools.generatorId();//初始化时，用户和地址公用一个id
        user.setId(id);
        sysUserDao.addUser(user);
    }


    public List<Map<String, Object>> getPermissionByRoleId(String user_id) {
        return sysUserDao.getPermissionByRoleId(Long.parseLong(user_id));
    }

    public List<Map<String, Object>> getUserList(
            Pager<Map<String, Object>> pager, Long userId, Long role_id, String key, Integer enable) throws CustomException {
        MySql sql = new MySql("select u.*, r.name role_name,u.realname ");
        sql.append("from sys_user u ");
        sql.append("LEFT JOIN role r ON u.role_id = r.id");
        if (role_id == -1) {//1
            sql.append("where  u.role_id !=?", RoleConstant.VERIFY_ID);
        } else {
            sql.append("where  u.role_id = ?", role_id);
        }
        sql.append("and u.id != ?", userId);
        sql.notNullAppend(" and u.status_ = ?", enable);
        key = SQLTools.FuzzyKey(key);
        sql.notNullAppend(" and (u.mobile like ?  or u.realname like ?)", key, key);
        sql.orderBy("u.status_ asc", "u.id desc");
        sql.limit(pager);
        List<Map<String, Object>> data = jdbcTemplate.queryForList(sql.toString(), sql.getValues());
        if (null != data && data.size() > 0) {
            SQLTools.removePass(data);
        }
        return data;
    }

    public boolean updateRole(String user_id, Integer role_id) throws CustomException {
        int effect = sysUserDao.updateUserRole(user_id, role_id);
        return effect > 0;
    }

    public boolean updateParentId(String user_id, Long parent_id) {
        int effect = sysUserDao.updateParentId(user_id, parent_id);
        return effect > 0;
    }

    public void updateStatus(String user_id, String status) throws CustomException {
        int effect = sysUserDao.updateUserStatus(user_id, status);
        if (effect == 0) throw new CustomException("修改失败");
    }

    public boolean updateMultiLogin(Long userId, Integer multi) {
        int effect = sysUserDao.updateMultiLogin(userId, multi);
        return effect > 0;
    }

    @Transactional
    public void updatePwd(String mobile, String newPwd) throws Exception {
        if (!StringUtils.isNotBlank(newPwd)) throw new CustomException("密码不能为空");
        int effect = userDao.updateUserPwd(mobile, newPwd);
        if (effect == 0) throw new CustomException("修改密码出错");
    }

    public void phoneHasEixst(String phone) throws CustomException {
        boolean b = sysUserDao.userExist(phone);
        if (b) {
            throw new CustomException("手机号已存在");
        }
    }

    @MyRedis(value = "sys_user#{0}",expire = 300)
    public User getUserByToken(String token) {
        return sysUserDao.getUserByToken(token);
    }


    public List<Map<String, Object>> getSales() throws CustomException {
        MySql sql = new MySql("select u.id,u.realname,u.mobile,u.status_,s.name storage_name ");
        sql.append("from sys_user u ");
        sql.append("LEFT JOIN storage s ");
        sql.append("on u.storage_id = s.id");
        sql.append("where u.role_id = ?", RoleConstant.SALE_ID);
        sql.orderBy("u.storage_id ");
        List<Map<String, Object>> data = jdbcTemplate.queryForList(sql.toString(), sql.getValues());
        if (null != data && data.size() > 0) {
            SQLTools.removePass(data);
        }
        return data;
    }
}
