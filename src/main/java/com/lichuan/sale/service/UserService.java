package com.lichuan.sale.service;

import com.lichuan.sale.annoation.MyRedis;
import com.lichuan.sale.core.CustomException;
import com.lichuan.sale.model.Role;
import com.lichuan.sale.model.User;
import com.lichuan.sale.model.UserAddress;
import com.lichuan.sale.model.Vip;
import com.lichuan.sale.result.Code;
import com.lichuan.sale.result.MultiResult;
import com.lichuan.sale.tools.StringUtils;
import com.lichuan.sale.tools.Tools;
import com.lichuan.sale.tools.sqltools.MySql;
import com.lichuan.sale.tools.sqltools.Pager;
import com.lichuan.sale.tools.sqltools.SQLTools;
import org.springframework.beans.factory.annotation.Autowired;
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
            if (user.getRole_id() == 0) throw new CustomException("正在审核中...");
            List<Role> role = commonDao.getRoles(user.getRole_id());

            //是否允许多用户登录
            if (StringUtils.isNull(user.getToken()) || user.getIs_multi() == 0) {
                String token = StringUtils.uuid();
                userDao.updateToken(user.getId(), token);
                user.setToken(token);
            }
            List<Map<String, Object>> permissionList = userDao.getPermissionByRoleId(user.getRole_id());
            Map<String, Object> notice = commonDao.getNoticeOfService();
            Map<String, Object> address = userDao.getAddress(user.getAddress_id().toString());

            data.put("permission", permissionList);
            data.put("user", user);
            data.put("roleList", role);
            data.put("notice", notice);
            data.put("address", address);
        } catch (Exception e) {
            throw new CustomException(e.getMessage());
        }
        return data;
    }

    public void addUser(User user, UserAddress userAddress) throws CustomException {
        if (userDao.userExist(user.getUsername())) {
            throw new CustomException("用户名或手机号已存在");
        }

        String encryptPass = Tools.encryptPass(user.getUsername(), user.getPassword());
        user.setPassword(encryptPass);
        Long id = Tools.generatorId();//初始化时，用户和地址公用一个id
        user.setId(id);
        user.setAddress_id(id);
        userAddress.setId(id);
        userAddress.setUser_id(id);

        userDao.addAddress(userAddress);
        userDao.addUser(user);
    }

    public Map<String, Object> getAddress(String address_id) throws CustomException {
        return userDao.getAddress(address_id);
    }

    public List<Map<String, Object>> getPermissionByRoleId(String user_id) {
        return userDao.getPermissionByRoleId(Long.parseLong(user_id));
    }

    public MultiResult<Map<String, Object>> getUserList(
            Pager<Map<String, Object>> pager, User user, Long role_id, String key, Integer enable) {

        MySql sql = new MySql("select u.*, r.name role_name,a.city,a.address,u.realname ");
        sql.append("from user u ");
        sql.append("LEFT JOIN role r ON u.role_id = r.id");
        sql.append("LEFT JOIN user_address a ON u.address_id = a.id ");
        sql.append("where 1 = 1 ");
        if (!auth.hasPermission(user.getRole_id(), AuthService.P_User_Manger)) {
            sql.notNullAppend(" and u.proxy_id= ?", user.getId());
        }
        sql.notNullAppend(" and u.status_ = ?", enable);
        key = SQLTools.FuzzyKey(key);
        sql.notNullAppend(" and (u.mobile like ? or a.address like ?  or a.city like ? or u.realname like ?)",
                key, key, key, key, key);
        sql.orderBy("u.status_ asc", "u.id desc");
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

    public void updateStatus(String user_id, String status) throws CustomException {
        int effect = userDao.updateUserStatus(user_id, status);
        if (effect == 0) throw new CustomException("修改失败");
    }

    public boolean updateMultiLogin(Long userId, Integer multi) {
        int effect = userDao.updateMultiLogin(userId, multi);
        return effect > 0;
    }

    @Transactional
    public void updatePwd(String mobile, String newPwd) throws Exception {
        if (!StringUtils.isNotBlank(newPwd)) throw new CustomException("密码不能为空");
        int effect = userDao.updateUserPwd(mobile, newPwd);
        if (effect == 0) throw new CustomException("修改密码出错");
    }

    public void phoneHasEixst(String phone) throws CustomException {
        boolean b = userDao.phoneHasEixst(phone);
        if (b) {
            throw new CustomException("手机号已存在");
        }
    }

    public void updateAddress(String user_id, String address) throws CustomException {
        int effect = userDao.updateUserAddress(user_id, address);
        if (effect == 0) throw new CustomException("修改失败");
    }

    public void updateUserCity(String user_id, String city) throws CustomException {
        int effect = userDao.updateUserCity(user_id, city);
        if (effect == 0) throw new CustomException("修改失败");
    }

    public String getProxyId(Long user_id) {
        return userDao.getProxyId(user_id);
    }


    @Autowired
    VipService mVipService;

    @MyRedis(value = "user#{0}", expire = 300)
    public User getUserByToken(String token) {
        User user = userDao.getUserByToken(token);
        if (user != null) {
            Vip userVipInfo = mVipService.getUserVipInfo(user.getId());
            user.setVip(userVipInfo);
        }
        return user;
    }

    public Map<String, Object> addressAndDistance(Long userId) throws CustomException {
        return userDao.addressAndDistance(userId);
    }

    public List<Map<String, Object>> addressList(Long userId) throws CustomException {
        return userDao.addressList(userId);
    }

    public List<Map<String, Object>> getUserAddress(Long userId) {
        return userDao.getUserAddress(userId);
    }

    public void deleteAddress(String address_id) {
        userDao.deleteAddress(address_id);
    }

    public void setDefaultAddress(String address_id) {
        userDao.setDefaultAddress(address_id);
    }

    public List<Map<String, Object>> getUserListByProxy(long proxy_id) {
        return userDao.getUserListByProxy(proxy_id);
    }
}
