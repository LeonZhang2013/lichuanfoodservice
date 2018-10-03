package com.lichuan.sale.dao;

import com.lichuan.sale.annoation.MyRedisDel;
import com.lichuan.sale.core.CustomException;
import com.lichuan.sale.model.User;
import com.lichuan.sale.tools.SqlInfo;
import com.lichuan.sale.tools.Tools;
import com.lichuan.sale.tools.sqltools.SQLTools;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class SysUserDao extends BaseDao {


    /**
     * 系统用户登陆
     *
     * @param username
     * @param pass
     * @return
     * @throws CustomException
     */
    public User sysLogin(String username, String pass) throws CustomException {
        pass = Tools.encryptPass(username, pass);
        String sql = "SELECT u.*,r.name role_name from sys_user u LEFT JOIN role r on u.role_id = r.id  where u.username=? limit 0,1";
        RowMapper<User> rowMap = new BeanPropertyRowMapper<User>(User.class);
        List<User> users = jdbcTemplate.query(sql, new Object[]{username}, rowMap);
        if (users.size() == 0) {
            return null;
        }
        //密码不相等返回空
        if (!users.get(0).getPassword().equals(pass)) {
            return null;
        }
        users.get(0).setPassword(null);
        return users.get(0);
    }


    public boolean userExist(String username) {
        String sql = "SELECT COUNT(id) from sys_user where username=? ";
        int count = jdbcTemplate.queryForObject(sql, new Object[]{username}, Integer.class);
        return count > 0;
    }

    public boolean phoneHasEixst(String phone) {
        String sql = "SELECT id from sys_user where mobile=? ";
        List<Map<String, Object>> users = jdbcTemplate.queryForList(sql, new Object[]{phone});
        return users.size() > 0;
    }

    public Long addUser(User user) {
        SqlInfo sqlInfo = SQLTools.getInsertSQL(user, "sys_user");
        jdbcTemplate.update(sqlInfo.getSql(), sqlInfo.getValues());
        return user.getId();
    }


    public List<Map<String, Object>> getPermissionByRoleId(long role_id) {
        String sql = "select p.* from permission p, role_permission r where r.role_id =? and r.`permission_id`  = p.id";
        List<Map<String, Object>> permission = jdbcTemplate.queryForList(sql, new Object[]{role_id});
        return permission;
    }


    public int updateUserRole(String user_id, Integer role_id) {
        String sql = "update sys_user set role_id=? where id = ?";
        int effect = jdbcTemplate.update(sql, role_id, user_id);
        return effect;
    }

    public int updateParentId(String user_id, Long parent_id) {
        String sql = "update sys_user set parent_id = ? where id = ?";
        int effect = jdbcTemplate.update(sql, parent_id, user_id);
        return effect;
    }

    public int updateUserStatus(String user_id, String status) {
        String sql = "update sys_user set status_=? where id = ?";
        int effect = jdbcTemplate.update(sql, status, user_id);
        return effect;

    }

    public int updateMultiLogin(Long userId, Integer multi) {
        int update = jdbcTemplate.update("update sys_user set is_multi = ? where id = ?", multi, userId);
        return update;
    }

    public int updateUserPwd(String mobile, String password) throws Exception {
        List<Map<String, Object>> maps = jdbcTemplate.queryForList("SELECT username FROM sys_user WHERE mobile = ?", mobile);
        password = Tools.encryptPass(maps.get(0).get("username").toString(), password);
        String sql = "update user set password=? where mobile = ?";
        int effect = jdbcTemplate.update(sql, password, mobile);
        return effect;
    }

    public int updateToken(Long id, String token) throws Exception {
        String sql = "update sys_user set token = ? where id = ?";
        int effect = jdbcTemplate.update(sql, token, id);
        return effect;
    }


    public Map<String, Object> getAddressByUserId(Long id) throws CustomException {
        String sql = "select * from `user_address`  where user_id=?";
        List<Map<String, Object>> maps = jdbcTemplate.queryForList(sql, id);
        if (maps.size() == 0) {
            throw new CustomException("该用户没有地址");
        }
        return maps.get(0);
    }


    public User getUserByToken(String token) {
        String sql = "SELECT * from sys_user where token= ? ";
        RowMapper<User> rowMap = new BeanPropertyRowMapper<User>(User.class);
        List<User> users = jdbcTemplate.query(sql, new Object[]{token}, rowMap);
        if (users.size() == 0) {
            return null;
        }
        return users.get(0);
    }


}
