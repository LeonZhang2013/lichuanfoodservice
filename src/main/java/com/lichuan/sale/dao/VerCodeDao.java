package com.lichuan.sale.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Repository
public class VerCodeDao extends BaseDao {

    public void saveCode(String phone, String verCode, Date expirseDate) {
        String sql = "insert into vercode (phone,code,expires_in) values(?,?,?)";
        jdbcTemplate.update(sql, phone, verCode, expirseDate);
    }

    public List<String> getVerCode(String phone) {
        String sql = "select code from vercode where phone=?";
        return jdbcTemplate.queryForList(sql, String.class, phone);
    }

    public int delCodeByPhone(String phone) {
        String sql = "DELETE from vercode where phone=?";
        return jdbcTemplate.update(sql, phone);
    }
}
