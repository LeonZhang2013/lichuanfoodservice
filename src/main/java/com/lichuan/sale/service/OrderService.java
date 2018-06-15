package com.lichuan.sale.service;

import com.lichuan.sale.core.CustomException;
import com.lichuan.sale.model.User;
import com.lichuan.sale.result.Code;
import com.lichuan.sale.result.MultiResult;
import com.lichuan.sale.tools.Arith;
import com.lichuan.sale.tools.Tools;
import com.lichuan.sale.tools.sqltools.MySql;
import com.lichuan.sale.tools.sqltools.Pager;
import com.lichuan.sale.tools.sqltools.SQLTools;
import constant.OrderStatus;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class OrderService extends BaseService {


    @Transactional
    public BigDecimal createOrder(Long user_id, Long order_id, String proxy_id, String remark, String address_id) throws Exception {
        checkRecommit(user_id);
        //获取购物车商品信息和数量
        List<Map<String, Object>> userCart = shopCartDao.getUserCart(user_id);

        //获取代理商仓库 距离基数
        String sql = "SELECT distance FROM storage s, user p where s.id = p.storage_id and p.id = ?";
        Long distance = jdbcTemplate.queryForObject(sql, new Object[]{proxy_id}, Long.class);

        BigDecimal totalPrice = (BigDecimal) Arith.calculationCharge(distance.toString(), userCart).get("totalPrice");

        int waitPay = OrderStatus.WAIT_PAY.getStatus();
        String orderSql = "INSERT INTO `order_` (`id`,`user_id`,`status_`,`address_id`,`total_price`,`remark`) values(?,?,?,?,?,?)";
        int update = jdbcTemplate.update(orderSql, order_id, user_id, waitPay, address_id, totalPrice, remark);
        if (update == 0) throw new CustomException("创建订单失败");

        MySql mySql = new MySql();
        mySql.append("INSERT INTO `order_item`(`user_id`,`product_id`,`piece_price`,`sub_freight`,`sub_price`,");
        mySql.append("`status_`,`buy_num`,`product_image`,`product_name`,`product_unit`,`order_id`,`proxy_id`)");
        mySql.append(" values(?,?,?,?,?,?,?,?,?,?,?,?)");
        int[] ints = jdbcTemplate.batchUpdate(mySql.toString(), new BatchPreparedStatementSetter() {
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Map<String, Object> item = userCart.get(i);
                ps.setObject(1, user_id);
                ps.setObject(2, item.get("product_id"));
                ps.setObject(3, item.get("price"));
                ps.setObject(4, item.get("sub_freight").toString());
                ps.setObject(5, item.get("sub_price").toString());
                ps.setObject(6, waitPay);
                ps.setObject(7, item.get("num"));
                ps.setObject(8, item.get("main_image"));
                ps.setObject(9, item.get("name"));
                ps.setObject(10, item.get("unit"));
                ps.setObject(11, order_id);
                ps.setObject(12, proxy_id);
            }

            public int getBatchSize() {
                return userCart.size();
            }
        });
        //清空购物车
        String sqlDel = "delete from shop_cart where user_id = ?";
        int effect = jdbcTemplate.update(sqlDel, user_id);
        if (effect == 0) throw new CustomException("清空购物车失败");

        return totalPrice;
    }

    //检查重复提交(在30秒内，不能提交两次)
    private void checkRecommit(Long userId) throws Exception {
        String sql = "SELECT create_time FROM `order_item` where user_id = ? ORDER BY create_time DESC LIMIT 0,1";
        List<Map<String, Object>> maps = jdbcTemplate.queryForList(sql, userId);
        if (maps.size() > 0) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = format.parse(maps.get(0).get("create_time").toString());
            long offset = System.currentTimeMillis() - date.getTime();
            if (offset < 10000) {//10秒内不能提交两次
                throw new CustomException("提交太快，休息一会");
            }
        }
    }

    public String getPaymentInfo(String s, BigDecimal totalPrice, String info) {
        return "";
    }

    public List<Map<String, Object>> getOrderStatus() throws CustomException {
        List<Map<String, Object>> data = OrderStatus.getOrderStatus();
        if (data.size() == 0) throw new CustomException("没有获取数据");
        return data;
    }


    public List<Map<String, Object>> getWxOrderList(Integer status, Pager<Map<String, Object>> pager, Long userId) {
        MySql mySql = new MySql();
        mySql.append("SELECT i.*,GROUP_CONCAT(i.product_image) images,o.total_price,o.remark FROM `order_` o,`order_item` i,`order_status` s");
        mySql.append("WHERE o.id = i.order_id and i.status_ = s.id and i.user_id=? and i.status_ = ?  GROUP BY o.id");
        mySql.orderByDesc("o.create_time");
        mySql.limit(pager);
        List<Map<String, Object>> result = jdbcTemplate.queryForList(mySql.toString(), userId,status);
        for(int i=0; i<result.size(); i++){
            result.get(i).put("images",result.get(i).get("images").toString().split(","));
        }
        return result;
    }

    public List<Map<String, Object>> getOrderList(Integer status, String key, Pager<Map<String, Object>> pager, User user) {
        MySql mySql = new MySql();
        mySql.append(" SELECT o.id sub_id,u.id user_id,o.order_id id,o.status_ ,u.realname,o.product_unit, a.mobile ,oo.remark,a.city,a.address,os.name status_name,");
        mySql.append("SUM(o.sub_price) `product_total_price`,SUM(o.buy_num) buy_num,SUM(o.sub_freight) total_freight,o.create_time  ");
        mySql.append(" FROM `user` u,`order_item` o,order_ oo,user_address a ,order_status os ");
        mySql.append(" WHERE u.id = o.user_id and a.user_id = u.id and os.id = o.status_ and oo.id = o.order_id ");
        mySql.notNullAppend("  and o.status_ = ? ", status);
        key = SQLTools.FuzzyKey(key);
        mySql.notNullAppend(" and (o.order_id like ? or u.realname like ?) ", key, key);
        //如果有管理订单的功能查询多订单，跳过括号内的sql 判断
        if (!auth.hasPermission(user.getRole_id(), auth.P_Order_ALL)) {
            mySql.notNullAppend(" and (u.id=? or u.proxy_id = ?) ", user.getId(), user.getId());
        }
        mySql.append("GROUP BY o.order_id");
        mySql.orderByDesc("o.create_time");
        mySql.limit(pager);
        List<Map<String, Object>> data = jdbcTemplate.queryForList(mySql.toString(), mySql.getValues());
        for (int i = 0; i < data.size(); i++) {
            Map<String, Object> order = data.get(i);
            order.put("create_time", Tools.priseTimestamp((Timestamp) order.get("create_time")));
            if (auth.hasPermission(user.getRole_id(), auth.P_Dispatching)) {//有权限
                switch (OrderStatus.getOrderStatus(status)) {
                    case WAIT_RECEIVE:
                        order.put("oper_status", "true");
                }
            }
            //所有人都可以付款
            switch (OrderStatus.getOrderStatus(status)) {
                case WAIT_PAY:
                    order.put("oper_status", "true");
            }
        }
        return data;
    }

    public List<Map<String, Object>> getOrderDetail(Long order_id, Integer status) {
        MySql mySql = new MySql();
        mySql.append("select o.*,os.name status_name ");
        mySql.append("from order_item o,`product` p,order_status os");
        mySql.append("where os.id = o.status_ ");
        mySql.append("and o.order_id = ?");
        mySql.addValue(order_id);
        mySql.notNullAppend("and o.status_ = ?", status);
        List<Map<String, Object>> maps = jdbcTemplate.queryForList(mySql.toString(), mySql.getValues());
        return maps;
    }

    public void operateOrder(Long orderId, Integer status) throws CustomException {
        int effect = orderDao.operateOrder(orderId, status);
        if (effect == 0) throw new CustomException("操作失败");
    }

    public void cancelOrder(Long order_id, Long user_id) throws CustomException {
        int effect = orderDao.cancelOrder(order_id, user_id);
        if (effect == 0) throw new CustomException("只能操作自己订单");
    }

    public Map<String,Object> getProductSaleNum(Long product_id) {
        return orderDao.getProductSaleNum(product_id);
    }

    public Object getSaleNum(String product_id) {
        return orderDao.getSaleNum(product_id);
    }

    public void paySuccess(String orderId) {
        orderDao.paySuccess(orderId);
    }
}
