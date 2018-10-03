package com.lichuan.sale.service;

import com.lichuan.sale.core.CustomException;
import com.lichuan.sale.model.SysUser;
import com.lichuan.sale.model.User;
import com.lichuan.sale.tools.Arith;
import com.lichuan.sale.tools.Tools;
import com.lichuan.sale.tools.sqltools.MySql;
import com.lichuan.sale.tools.sqltools.Pager;
import com.lichuan.sale.tools.sqltools.SQLTools;
import com.lichuan.sale.constant.OrderStatus;
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
    public BigDecimal createOrder(User user, Long order_id, String remark, String address_id) throws Exception {
        checkRecommit(user.getId());
        //获取购物车商品信息和数量
        List<Map<String, Object>> userCart = shopCartDao.getUserCart(user.getId());

        //获取代理商仓库 距离基数
        String sql = "SELECT ua.*,s.distance FROM storage s, user_address ua where s.id = ua.storage_id and ua.id = ?";
        Map<String, Object> address = jdbcTemplate.queryForList(sql, address_id).get(0);

        BigDecimal totalPrice = (BigDecimal) Arith.calculationCharge(address.get("distance").toString(), userCart,user.getVip()).get("totalPrice");

        int waitPay = OrderStatus.WAIT_PAY.getStatus();
        String orderSql = "INSERT INTO `order_` (`id`,`user_id`,`status_`,`total_price`,`remark`" +
                ",city,address,user_name,mobile,storage_id,proxy_id,discount) values(?,?,?,?,?,?,?,?,?,?,?,?)";

        int update = jdbcTemplate.update(orderSql, order_id, user.getId(), waitPay, totalPrice, remark,
                address.get("city"), address.get("address"), address.get("name"), address.get("mobile"),
                address.get("storage_id"), user.getProxy_id(),user.getVip().getDiscount());
        if (update == 0) throw new CustomException("创建订单失败");

        MySql mySql = new MySql();
        mySql.append("INSERT INTO `order_item`(`user_id`,`product_id`,`piece_price`,`sub_freight`,`sub_price`,");
        mySql.append("`buy_num`,`product_image`,`product_name`,`product_unit`,`category_id`,`order_id`)");
        mySql.append(" values(?,?,?,?,?,?,?,?,?,?,?)");
        int[] ints = jdbcTemplate.batchUpdate(mySql.toString(), new BatchPreparedStatementSetter() {
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Map<String, Object> item = userCart.get(i);
                ps.setObject(1, user.getId());
                ps.setObject(2, item.get("product_id"));
                ps.setObject(3, item.get("price"));
                ps.setObject(4, item.get("sub_freight").toString());
                ps.setObject(5, item.get("sub_price").toString());
                ps.setObject(6, item.get("num"));
                ps.setObject(7, item.get("main_image"));
                ps.setObject(8, item.get("name"));
                ps.setObject(9, item.get("unit"));
                ps.setObject(10, item.get("category_id"));
                ps.setObject(11, order_id);
            }

            public int getBatchSize() {
                return userCart.size();
            }
        });
        //清空购物车
        String sqlDel = "delete from shop_cart where user_id = ?";
        int effect = jdbcTemplate.update(sqlDel, user.getId());
        if (effect == 0) throw new CustomException("清空购物车失败");

        return totalPrice;
    }

    //检查重复提交(在10秒内，不能提交两次)
    private void checkRecommit(Long userId) throws Exception {
        String sql = "SELECT create_time FROM `order_` where user_id = ? ORDER BY create_time DESC LIMIT 0,1";
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


    public List<Map<String, Object>> getOrderStatus() throws CustomException {
        List<Map<String, Object>> data = OrderStatus.getOrderStatus();
        if (data.size() == 0) throw new CustomException("没有获取数据");
        return data;
    }


    public List<Map<String, Object>> getWxOrderList(Integer status, Pager<Map<String, Object>> pager, Long userId) {
        MySql mySql = new MySql();
        mySql.append("SELECT i.*,o.create_time,o.status_,GROUP_CONCAT(i.product_image) images,o.total_price,o.remark ");
        mySql.append("FROM `order_` o,`order_item` i,`order_status` s");
        mySql.append("WHERE o.id = i.order_id and o.status_ = s.id and i.user_id=? and o.status_ = ?  GROUP BY o.id");
        mySql.orderByDesc("o.create_time");
        mySql.limit(pager);
        List<Map<String, Object>> result = jdbcTemplate.queryForList(mySql.toString(), userId, status);
        for (int i = 0; i < result.size(); i++) {
            result.get(i).put("images", result.get(i).get("images").toString().split(","));
        }
        return result;
    }

    public List<Map<String, Object>> getOrderList(Integer status, String key, Pager<Map<String, Object>> pager, SysUser user) throws CustomException {
        MySql mySql = new MySql();

        mySql.append("SELECT i.id sub_id,o.user_id,i.order_id id,o.status_ ,o.`user_name` realname,i.product_unit,");
        mySql.append(" o.mobile,o.remark,o.city,o.address,os.name status_name,SUM(i.sub_price) `product_total_price`, ");
        mySql.append("SUM(i.buy_num) buy_num,SUM(i.sub_freight) total_freight,o.create_time,o.deliver_ok,su.realname proxy_name");
        mySql.append("FROM `order_item` i   LEFT JOIN order_ o  on o.id = i.order_id");
        mySql.append("LEFT JOIN  order_status os on  os.id = o.status_");
        mySql.append("LEFT JOIN  sys_user su on  su.id = o.proxy_id");
        mySql.append(" WHERE o.status_ = ?  ", status);
        key = SQLTools.FuzzyKey(key);
        mySql.notNullAppend(" and  o.user_name like ? ", key);
        //如果有管理订单的功能查询多订单，跳过括号内的sql 判断
        if (!auth.hasPermission(user.getRole_id(), auth.P_Order_ALL)) {
            mySql.notNullAppend("  and o.`proxy_id`  = ? ", user.getId());
        }
        mySql.append("GROUP BY o.id");
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

    public List<Map<String, Object>> getOrderItem(Long order_id, Integer status) {
        MySql mySql = new MySql();
        mySql.append("select i.*,os.name status_name ");
        mySql.append("from order_item i,order_ o,order_status os");
        mySql.append("where os.id = o.status_ and o.id = i.order_id");
        mySql.append("and o.id = ?");
        mySql.addValue(order_id);
        mySql.notNullAppend("and o.status_ = ?", status);
        List<Map<String, Object>> maps = jdbcTemplate.queryForList(mySql.toString(), mySql.getValues());
        return maps;
    }

    public List<Map<String, Object>> getOrderItem(Long order_id) {
        MySql mySql = new MySql();
        mySql.append("select *  from order_item  where order_id =?");
        List<Map<String, Object>> maps = jdbcTemplate.queryForList(mySql.toString(), order_id);
        return maps;
    }

    public void operateOrder(Long orderId, Integer status) throws CustomException {
        int effect = orderDao.operateOrder(orderId, status);
        if (effect == 0) throw new CustomException("操作失败");
    }

    public void cancelOrder(Long order_id) throws CustomException {
        int effect = orderDao.cancelOrder(order_id.toString());
        if (effect == 0) throw new CustomException("操作失败");
    }

    public Map<String, Object> getProductSaleNum(Long product_id) {
        return orderDao.getProductSaleNum(product_id);
    }

    public Object getSaleNum(String product_id) {
        return orderDao.getSaleNum(product_id);
    }

    public void paySuccess(String orderId, String out_trade_no) {
        orderDao.paySuccess(orderId, out_trade_no);
    }


    public Map<String, Object> getOrderDetail(Long order_id) {
        return orderDao.getOrderDetail(order_id);
    }

    public String goodsOk(Long order_id) throws CustomException {
        List<Map<String, Object>> orderItemNum = orderDao.getOrderItemNum(order_id);
        for (int i = 0; i < orderItemNum.size(); i++) {
            Map<String, Object> item = orderItemNum.get(i);
            if ((int) item.get("buy_num") > (int) item.get("give_num")) {
                throw new CustomException(item.get("product_name") + "尚未完成");
            }
        }
        orderDao.operateOrder(order_id, OrderStatus.WAIT_RECEIVE.getStatus());
        return "操作完成";
    }


}
