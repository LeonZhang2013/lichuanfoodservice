package com.lichuan.sale.dao;

import com.lichuan.sale.tools.sqltools.MySql;
import constant.OrderStatus;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Repository
public class OrderDao extends BaseDao{

    public int operateOrder(Long orderId, Integer status) {
        String sql = "update order_item set status = ? where order_id = ? and status = ?";
        return jdbcTemplate.update(sql, OrderStatus.getOrderStatus(status).next().getStatus(), orderId, status);
    }

    public int cancelOrder(Long order_id, Long user_id) {
        MySql mySql = new MySql();
        mySql.append("update order_item set `order_status` = ? where ");
        mySql.addValue(OrderStatus.CANCEL.getStatus());
        mySql.notNullAppend(" order_id = ? and user_id = ?", order_id,user_id);
        return jdbcTemplate.update(mySql.toString(), mySql.getValues());
    }

    public Map<String,Object> getProductSaleNum(Long product_id) {
        List<Map<String, Object>> maps = jdbcTemplate.queryForList("SELECT SUM(buy_num) sale_num FROM `order_item` WHERE `product_id` = ?", product_id);
        if(maps.size()>0){
            return maps.get(0);
        }else{
            Map data =  new HashMap<String,Object>();
            data.put("sale_num",0);
            return data;
        }
    }
}
