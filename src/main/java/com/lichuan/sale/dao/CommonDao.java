package com.lichuan.sale.dao;

import com.alibaba.fastjson.JSONArray;
import com.lichuan.sale.model.Role;
import com.lichuan.sale.model.Version;
import com.lichuan.sale.constant.LcConstant;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;


@Repository
public class CommonDao {

    @Resource
    JdbcTemplate jdbcTemplate;

    /**
     * 检查更新
     *
     * @param code
     * @param packageName
     * @return
     * @throws Exception
     */
    public Version checkVersion(Integer code, String packageName) throws Exception {
        String sql = "select * from version where package_name like ? order by id desc limit 0,1";
        List<Version> versionList = jdbcTemplate.query(sql, new String[]{packageName}, new BeanPropertyRowMapper<Version>(Version.class));
        if (null != versionList && versionList.size() > 0) {
            Version version = versionList.get(0);
            if (code < version.getCode()) {
                return version;
            }
        }
        return null;
    }

    public List<Role> getRoles(Long role_id) {
        String sql = "select * from role where (select `level`  from role where id=?) < level";
        List<Role> roleList = jdbcTemplate.query(sql, new Object[]{role_id}, new BeanPropertyRowMapper<Role>(Role.class));
        return roleList;
    }

    public List<Role> getRoles() {
        String sql = "select * from role ";
        List<Role> roleList = jdbcTemplate.query(sql, new BeanPropertyRowMapper<Role>(Role.class));
        return roleList;
    }

    public Role getRole(Long role_id) {
        String sql = "select * from role where id = ?";
        List<Role> roleList = jdbcTemplate.query(sql, new Object[]{role_id}, new BeanPropertyRowMapper<Role>(Role.class));
        return roleList.get(0);
    }

    public Map<String, Object> getNoticeOfService() {
        String sql = "select * from notice where isShow = 1 and type= 0 order by id desc limit 0,1";
        List<Map<String, Object>> maps = jdbcTemplate.queryForList(sql);
        return maps.size() > 0 ? maps.get(0) : null;
    }

    public Map<String, Object> getNoticeOfClient() {
        String sql = "select * from notice where isShow = 1 and type= 1 order by id desc limit 0,1";
        List<Map<String, Object>> maps = jdbcTemplate.queryForList(sql);
        return maps.size() > 0 ? maps.get(0) : null;
    }

    public List<Map<String, Object>> getPermissions() {
        return jdbcTemplate.queryForList("SELECT * FROM permission");
    }

    @Transactional
    public void updatePermissions(Long userId, String role_id, JSONArray groupJson) {
        jdbcTemplate.update("delete from role_permission where role_id = ?", role_id);
        for (int i = 0; i < groupJson.size(); i++) {
            Object permission_id = groupJson.getJSONObject(i).get("id");
            jdbcTemplate.update("insert into role_permission (role_id,permission_id,update_uid) values (?,?,?)", role_id, permission_id, userId);
        }
    }

    public List<Map<String, Object>> getMyPermissions(String role_id) {
        return jdbcTemplate.queryForList("select * from role_permission where role_id = ?", role_id);
    }


    public List<Map<String, Object>> getArea(Long parentId) {
        return jdbcTemplate.queryForList("select * from area where enable=1 and parent_id = ?", parentId);
    }


    public Map<String, Role> getRoleMap() {
        Map<String, Role> roleMap = LcConstant.getRoleMap();
        if (roleMap.size() == 0) {
            List<Role> roleList = getRoles();
            for (int i = 0; i < roleList.size(); i++) {
                roleMap.put(roleList.get(i).getId() + "", roleList.get(i));
            }
        }
        return roleMap;
    }
}
