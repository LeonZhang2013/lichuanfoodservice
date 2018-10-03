package com.lichuan.sale.dao;

import com.lichuan.sale.configurer.RoleConstant;
import com.lichuan.sale.core.CustomException;
import com.lichuan.sale.model.ChatRecord;
import com.lichuan.sale.model.User;
import com.lichuan.sale.model.UserAddress;
import com.lichuan.sale.tools.SqlInfo;
import com.lichuan.sale.tools.Tools;
import com.lichuan.sale.tools.sqltools.MySql;
import com.lichuan.sale.tools.sqltools.Pager;
import com.lichuan.sale.tools.sqltools.SQLTools;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class UserDao extends BaseDao {

    /**
     * 微信用户
     *
     * @param username
     * @param pass
     * @return
     * @throws Exception
     */
    public User login(String username, String pass) throws Exception {
        pass = Tools.encryptPass(username, pass);
        String sql = "SELECT u.*,r.name role_name from USER u LEFT JOIN role r on u.role_id = r.id  where u.username=? limit 0,1";
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
        String sql = "SELECT COUNT(id) from USER where username=? ";
        int count = jdbcTemplate.queryForObject(sql, new Object[]{username}, Integer.class);
        return count > 0;
    }

    public boolean phoneHasEixst(String phone) {
        String sql = "SELECT id from USER where mobile=? ";
        List<Map<String, Object>> users = jdbcTemplate.queryForList(sql, new Object[]{phone});
        return users.size() > 0;
    }

    public Long addUser(User user) {
        SqlInfo sqlInfo = SQLTools.getInsertSQL(user);
        jdbcTemplate.update(sqlInfo.getSql(), sqlInfo.getValues());
        return user.getId();
    }

    public Long addAddress(UserAddress userAddress) {
        SqlInfo sqlInfo = SQLTools.getInsertSQL(userAddress, "user_address");
        jdbcTemplate.update(sqlInfo.getSql(), sqlInfo.getValues());
        return userAddress.getId();
    }

    public Map<String, Object> getAddress(String address_id) throws CustomException {
        String sql = "select * from `user_address`  where id=?";
        List<Map<String, Object>> maps = jdbcTemplate.queryForList(sql, address_id);
        if (maps.size() == 0) {
            throw new CustomException("该用户没有地址");
        }
        return maps.get(0);
    }


    public List<Map<String, Object>> getPermissionByRoleId(long role_id) {
        String sql = "select p.* from permission p, role_permission r where r.role_id =? and r.`permission_id`  = p.id";
        List<Map<String, Object>> permission = jdbcTemplate.queryForList(sql, new Object[]{role_id});
        return permission;
    }


    public int updateUserRole(String user_id, Integer role_id) {
        String sql = "update user set role_id=? where id = ?";
        int effect = jdbcTemplate.update(sql, role_id, user_id);
        return effect;
    }

    public int updateParentId(String user_id, Long parent_id) {
        String sql = "update user set parent_id = ? where id = ?";
        int effect = jdbcTemplate.update(sql, parent_id, user_id);
        return effect;
    }

    public int updateUserStatus(String user_id, String status) {
        String sql = "update user set status_=? where id = ?";
        int effect = jdbcTemplate.update(sql, status, user_id);
        return effect;

    }

    public int updateMultiLogin(Long userId, Integer multi) {
        int update = jdbcTemplate.update("update user set is_multi = ? where id = ?", multi, userId);
        return update;
    }

    public int updateUserPwd(String mobile, String password) throws Exception {
        List<Map<String, Object>> maps = jdbcTemplate.queryForList("SELECT username FROM user WHERE mobile = ?", mobile);
        password = Tools.encryptPass(maps.get(0).get("username").toString(), password);
        String sql = "update user set password=? where mobile = ?";
        int effect = jdbcTemplate.update(sql, password, mobile);
        return effect;
    }


//    @MyRedisDel("user#{1}")
    public int updateToken(Long id, String token) throws Exception {
        String sql = "update user set token = ? where id = ?";
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

    public int updateUserAddress(String user_id, String address) {
        String sql = "update user_address set address=? where id = (SELECT address_id from user where id=?) ";
        int effect = jdbcTemplate.update(sql, address, user_id);
        return effect;
    }

    public int updateUserCity(String user_id, String city) {
        String sql = "update user_address set city=? where id = (SELECT address_id from user where id=?) ";
        int effect = jdbcTemplate.update(sql, city, user_id);
        return effect;
    }

    public String getProxyId(Long user_id) {
        String sql = "select proxy_id from USER WHERE id = ?";
        String id = jdbcTemplate.queryForObject(sql, new Object[]{user_id}, String.class);
        return id;
    }

    public User getUserByToken(String token) {
        String sql = "SELECT * from USER where token=?";
        RowMapper<User> rowMap = new BeanPropertyRowMapper<User>(User.class);
        List<User> users = jdbcTemplate.query(sql, new Object[]{token}, rowMap);
        if (users.size() == 0) {
            return null;
        }
        return users.get(0);
    }

    public User getUserByXcxId(String openid) {
        String sql = "SELECT * from USER where xcx_id=? ";
        RowMapper<User> rowMap = new BeanPropertyRowMapper<User>(User.class);
        List<User> users = jdbcTemplate.query(sql, new Object[]{openid}, rowMap);
        if (users.size() == 0) {
            return null;
        }
        return users.get(0);

    }

    public Map<String, Object> addressAndDistance(Long userId) throws CustomException {
        String sql = "select ua.*,s.distance FROM `storage` s,`user` u,user_address ua " +
                " WHERE u.id = ? and ua.storage_id =s.id and ua.id = u.address_id";
        List<Map<String, Object>> maps = jdbcTemplate.queryForList(sql, userId);
        if (maps.size() == 0) throw new CustomException("没有设置收货地址");
        return maps.get(0);
    }


    public List<Map<String, Object>> addressList(Long userId) throws CustomException {
        String sql = "select * FROM user_address WHERE user_id = ? ";
        List<Map<String, Object>> maps = jdbcTemplate.queryForList(sql, userId);
        return maps;
    }

    public List<Map<String, Object>> getProxyByStorageId(String storageId) throws CustomException {
        String sql = "select id,realname,mobile from sys_user where role_id = ? and status_ = 1 and storage_id = ?";
        List<Map<String, Object>> maps = jdbcTemplate.queryForList(sql, RoleConstant.SALE_ID, storageId);
        if (maps.size() == 0) throw new CustomException("没有关联业务员");
        return maps;
    }


    public List<Map<String, Object>> getUserAddress(Long userId) {
        String sql = "select * from user_address where user_id = ?";
        List<Map<String, Object>> maps = jdbcTemplate.queryForList(sql, userId);
        return maps;
    }

    public void deleteAddress(String address_id) {
        String sql = "DELETE FROM `user_address` WHERE `id` =?";
        jdbcTemplate.update(sql, address_id);
    }

    public void setDefaultAddress(String address_id) {
        String sql = "update `user` set address_id = ?";
        jdbcTemplate.update(sql, address_id);
    }

    public User getUserByPhone(String phone) {
        String sql = "SELECT * from user where mobile=? limit 0,1";
        RowMapper<User> rowMap = new BeanPropertyRowMapper<User>(User.class);
        List<User> users = jdbcTemplate.query(sql, new Object[]{phone}, rowMap);
        if (users.size() == 0) {
            return null;
        }
        return users.get(0);
    }

    public List<Map<String, Object>> getUserListByProxy(long proxy_id) {

        String sql = "select u.id,u.realname,su.realname proxy_name,ua.address " +
                "from user u,sys_user su,user_address ua " +
                "where ua.user_id = u.id and u.proxy_id = su.id and u.proxy_id = ?";
        List<Map<String, Object>> maps = jdbcTemplate.queryForList(sql, proxy_id);
        return maps;
    }

    public List<Map<String, Object>> getClientChat(String user_id) throws CustomException {
        MySql sql = new MySql();
        sql.append("Select * from chat_record ");
        sql.append("where user_id = ?", user_id);
        sql.orderBy("create_time");
        sql.limit(new Pager<>());

        List<Map<String, Object>> maps = jdbcTemplate.queryForList(sql.toString(), sql.getValues());
        return maps;
    }

    public long setClientChat(ChatRecord record) {
        SqlInfo sqlInfo = new SQLTools().getInsertSQL(record, "chat_record");
        return jdbcTemplate.update(sqlInfo.getSql(), sqlInfo.getValues());
    }
}
