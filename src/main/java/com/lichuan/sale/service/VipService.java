package com.lichuan.sale.service;


import com.lichuan.sale.annoation.MyRedis;
import com.lichuan.sale.model.Vip;
import com.lichuan.sale.tools.SqlInfo;
import com.lichuan.sale.tools.sqltools.SQLTools;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class VipService extends BaseService {

    public List<Map<String, Object>> getVip() {
        String sql = "select * from vip";
        List<Map<String, Object>> maps = jdbcTemplate.queryForList(sql);
        return maps;
    }

    public Boolean addVip(Vip vip) {
        SqlInfo insertSQL = SQLTools.getInsertSQL(vip);
        int update = jdbcTemplate.update(insertSQL.getSql(), insertSQL.getValues());
        return update == 1;
    }

    public Boolean updateVip(Vip vip) {
        SqlInfo insertSQL = SQLTools.getUpdateById(vip, vip.getId());
        int update = jdbcTemplate.update(insertSQL.getSql(), insertSQL.getValues());
        return update == 1;
    }

    public Boolean deleteVip(Long id) {
        String sql = "delete from vip where id = ?";
        int update = jdbcTemplate.update(sql, id);
        return update == 1;
    }

    @MyRedis(value = "user_vip#{0}",expire = 300)
    public Vip getUserVipInfo(Long id) {
        String sql = "SELECT * FROM `vip` p WHERE p.`integral` < (SELECT SUM(`total_price`)  FROM `order_` WHERE `status_` = 3 and `user_id`  = ?) ORDER BY `integral` DESC";
        RowMapper<Vip> rowMap = new BeanPropertyRowMapper<Vip>(Vip.class);
        List<Vip> users = jdbcTemplate.query(sql, new Object[]{id}, rowMap);
        return users.get(0);

    }
}
