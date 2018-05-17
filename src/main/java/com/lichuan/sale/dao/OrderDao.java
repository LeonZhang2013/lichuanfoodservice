package com.lichuan.sale.dao;

import com.lichuan.sale.tools.sqltools.MySql;
import constant.OrderStatus;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;


@Repository
public class OrderDao extends BaseDao{

    public int operateOrder(Long orderId, Integer status) {
        String sql = "update user_order set order_status = ? where order_id = ? and order_status = ?";
        return jdbcTemplate.update(sql, OrderStatus.getOrderStatus(status).next().getStatus(), orderId, status);
    }

    public int cancelOrder(Long order_id, Long user_id) {
        MySql mySql = new MySql();
        mySql.append("update user_order set `order_status` = ? where ");
        mySql.addValue(OrderStatus.CANCEL.getStatus());
        mySql.notNullAppend(" order_id = ? and user_id = ?", order_id,user_id);
        return jdbcTemplate.update(mySql.toString(), mySql.getValues());
    }
}
