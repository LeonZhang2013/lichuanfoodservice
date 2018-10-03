package com.lichuan.sale.dao;

import com.lichuan.sale.core.CustomException;
import com.lichuan.sale.tools.sqltools.MySql;
import com.lichuan.sale.constant.OrderStatus;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Repository
public class OrderDao extends BaseDao {

    public int operateOrder(Long orderId, Integer status) {
        String sql = "update order_ set status_ = ? where order_id = ? and status_ = ?";
        return jdbcTemplate.update(sql, OrderStatus.getOrderStatus(status).next().getStatus(), orderId, status);
    }

    @Transactional
    public int cancelOrder(String order_id) throws CustomException {
        return operationOrder(order_id, OrderStatus.CANCEL.getStatus());
    }

    private int operationOrder(String order_id, Integer status) throws CustomException {
        MySql mySql = new MySql();
        mySql.append("update order_ set `status_` = ? where ", status.toString());
        mySql.append(" id = ? ", order_id);
        return jdbcTemplate.update(mySql.toString(), mySql.getValues());
    }


    public Map<String, Object> getProductSaleNum(Long product_id) {
        List<Map<String, Object>> maps = jdbcTemplate.queryForList("SELECT SUM(buy_num) sale_num FROM `order_item` WHERE `product_id` = ?", product_id);
        if (maps.size() > 0) {
            return maps.get(0);
        } else {
            Map data = new HashMap<String, Object>();
            data.put("sale_num", 0);
            return data;
        }
    }

    public Object getSaleNum(String product_id) {
        String sql = "select SUM(buy_num) num from order_item where product_id = ?";
        List<Map<String, Object>> maps = jdbcTemplate.queryForList(sql, product_id);
        if (maps.size() > 0) {
            return maps.get(0).get("num");
        }
        return null;
    }

    public String getOrderPrice(String order_id) {
        String sql = "select total_price from order_ where id =  ? and status_ = 0";
        List<Map<String, Object>> maps = jdbcTemplate.queryForList(sql, order_id);
        if (maps.size() > 0) {
            return maps.get(0).get("total_price").toString();
        }
        return null;
    }

    public int paySuccess(String orderId, String out_trade_no) {
        String sql = "update order_ set status_ = 1,platform_no=? where id = ? and status_ = 0";
        int update = jdbcTemplate.update(sql, out_trade_no, orderId);
        return update;
    }

    public Map<String, Object> getOrderDetail(Long order_id) {
        String sql = "select o.*,o.status_ status from order_ o where o.id = ?";
        List<Map<String, Object>> maps = jdbcTemplate.queryForList(sql, order_id);
        return maps.get(0);
    }

    public List<Map<String, Object>> getOrderItemNum(Long order_id) {
        String sql = "select product_name,buy_num, give_num from order_item where order_id = ?";
        List<Map<String, Object>> maps = jdbcTemplate.queryForList(sql, order_id);
        return maps;
    }


}
