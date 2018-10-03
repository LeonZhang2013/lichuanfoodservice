package com.lichuan.sale.dao;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lichuan.sale.configurer.StorageStatus;
import com.lichuan.sale.core.CustomException;
import com.lichuan.sale.model.ProductStatus;
import com.lichuan.sale.model.User;
import com.lichuan.sale.tools.Tools;
import com.lichuan.sale.tools.sqltools.MySql;
import com.lichuan.sale.tools.sqltools.Pager;
import com.lichuan.sale.tools.sqltools.SQLTools;
import com.lichuan.sale.constant.OrderStatus;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Repository
public class DeliverDao extends BaseDao {


    public List<Map<String, Object>> getDeliverProduct(User proxy) throws CustomException {
        MySql mySql = new MySql();
        mySql.append("SELECT i.`product_id` ,i.`product_image`,i.`product_name`,");
        mySql.append("SUM(i.`buy_num`-i.`give_num`) buy_num,   i.product_unit");
        mySql.append("FROM `order_` o, `order_item` i  ");
        mySql.append("WHERE o.`id` =i.`order_id` and o.`status_` = 1 and o.`deliver_ok` <> 1");
        mySql.append("and `proxy_id` = ?  GROUP BY i.`product_id`", proxy.getId());

        List<Map<String, Object>> maps = jdbcTemplate.queryForList(mySql.toString(), mySql.getValues());
        return maps;
    }

    public List<Map<String, Object>> getDeliverProduct1(User porxy) throws CustomException {
        MySql mySql = new MySql();
        Long storage_id = porxy.getStorage_id();

        mySql.append("SELECT s.*,b.num from");
        mySql.append("(SELECT i.`product_id` ,i.`product_image`,i.`product_name`,SUM(i.`buy_num`-i.`give_num`) buy_num, ");
        mySql.append("i.product_unit FROM `order_` o, `order_item` i");
        mySql.append("WHERE o.`id` =i.`order_id` and o.`status_` = ?", OrderStatus.WAIT_RECEIVE.getStatus());
        mySql.append("and `storage_id` = ? GROUP BY i.`product_id`) s", storage_id);
        mySql.append("left join");
        mySql.append("(SELECT `product_id`,num FROM `storage_product` s");
        mySql.append("WHERE s.`storage_id` = ? ) b", storage_id);
        mySql.append("on s.product_id= b.product_id");

        List<Map<String, Object>> maps = jdbcTemplate.queryForList(mySql.toString(), mySql.getValues());
        return maps;
    }

    public List<Map<String, Object>> getDeliverOrderList(User user, String key) throws CustomException {
        MySql mySql = new MySql();
        mySql.append("SELECT o.id order_id ,o.user_name name,o.city,o.address,o.mobile,o.create_time,o.update_time,o.deliver_ok ");
        mySql.append("FROM `order_item` i,`order_` o");
        mySql.append("WHERE o.id = i.order_id and o.status_ = ? ", OrderStatus.WAIT_RECEIVE.getStatus());
        mySql.append("and o.deliver_ok <> 1 and o.`storage_id` = ? ", user.getStorage_id());
        String keys = SQLTools.FuzzyKey(key);
        mySql.notNullAppend("and (o.address like ? or o.user_name like ?)", keys, keys);
        mySql.append("GROUP BY `order_id` ORDER BY o.create_time");
        List<Map<String, Object>> maps = jdbcTemplate.queryForList(mySql.toString(), mySql.getValues());

        return maps;
    }

    public List<Map<String, Object>> getUserOrderInfo(String order_id) {
        return jdbcTemplate.queryForList("SELECT * FROM `order_item` WHERE `order_id` = ?", order_id);
    }


    /**
     * @param deliverJson json 参数 product_id, buy_num, deliver_num
     * @param order_id
     */
    public void deliverOk(String deliverJson, String order_id) {
        JSONArray list = JSON.parseArray(deliverJson);
        MySql mySql = new MySql();

        final boolean[] isDeliverOk = {true};
        mySql.append("UPDATE `order_item` set `give_num` = ?  WHERE order_id =? and `product_id` = ?");
        int[] ints = jdbcTemplate.batchUpdate(mySql.toString(), new BatchPreparedStatementSetter() {
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                JSONObject item = (JSONObject) list.get(i);
                int deliver_num = item.getIntValue("deliver_num");
                int buy_num = item.getIntValue("buy_num");
                if (buy_num != deliver_num) isDeliverOk[0] = false;
                ps.setObject(1, deliver_num);
                ps.setObject(2, order_id);
                ps.setObject(3, item.get("product_id"));
            }

            public int getBatchSize() {
                return list.size();
            }
        });
        if (isDeliverOk[0]) {
            jdbcTemplate.update("UPDATE `order_` set `deliver_ok` = 1 WHERE `id` = ? ", order_id);
        } else {
            jdbcTemplate.update("UPDATE `order_` set `deliver_ok` = ? WHERE `id` = ? ", System.currentTimeMillis(), order_id);
        }

    }

    public List<Map<String, Object>> getReceiveOrderInfo(Long order_id) {
        MySql mySql = new MySql();
        mySql.append("SELECT i.*,p.name product_name,p.unit,p.main_image product_image ");
        mySql.append("FROM `storage_order` o,`storage_order_item` i,product p");
        mySql.append("WHERE o.id = i.order_id and p.id = i.product_id and o.id = ?");
        return jdbcTemplate.queryForList(mySql.toString(), order_id);
    }


    //查询在售商品
    public List<Map<String, Object>> getShoppingProduct() {
        return jdbcTemplate.queryForList("SELECT id product_id,name product_name,main_image product_image,unit FROM `product` WHERE `status_` =?", ProductStatus.SHOW);
    }

    /**
     * 获取 客户已下单的商品数。
     *
     * @param storageId 传空代表获取所有
     * @return
     * @throws CustomException
     */
    public List<Map<String, Object>> getUserOrderNum(Long storageId) throws CustomException {
        MySql mySql = new MySql();
        mySql.append("SELECT `product_id`,`product_name`, product_image,SUM(`buy_num`-`give_num`) order_num,product_unit unit ");
        mySql.append("from `order_item` i, order_ o");
        mySql.append("WHERE o.id = i.order_id and o.`status_` = ? ", OrderStatus.WAIT_RECEIVE.getStatus());
        mySql.notNullAppend("and o.storage_id = ? ", storageId);
        mySql.append("GROUP BY `product_id`");
        return jdbcTemplate.queryForList(mySql.toString(), mySql.getValues());
    }

    //获取 公司发给仓库，待接收商品 和 和补仓信息
    public List<Map<String, Object>> getStorageProductNum(Long storageId) throws CustomException {
        MySql mySql = new MySql();
        mySql.append("SELECT sp.product_id,p.name product_name,p.main_image product_image,sp.num, p.unit");
        mySql.append("FROM storage_product sp,product p WHERE p.id = sp.product_id and sp.storage_id = ?", storageId);
        List<Map<String, Object>> storage = jdbcTemplate.queryForList(mySql.toString(), storageId);
        return storage;
    }

    public void updateStorageNum(List<Object> list, Long storageId) throws CustomException {
        String update = "UPDATE `storage_product` set `num` = `num`+? WHERE `product_id` = ? and `storage_id` = ?";
        int[] oks = updateStorageNum(update, list, storageId);
        List<Object> insert = new ArrayList<>();
        for (int i = 0; i < oks.length; i++) {
            if (oks[i] == 0) insert.add(list.get(i));
        }
        if (insert.size() > 0) {
            String sql = "insert into storage_product (num,product_id,storage_id) values (?,?,?)";
            int[] ints = updateStorageNum(sql, insert, storageId);
            for (int i = 0; i < ints.length; i++) {
                if (ints[i] == 0) throw new CustomException("插入失败");
            }
        }
    }

    public int[] updateStorageNum(String sql, List<Object> list, Long storageId) {
        return jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Map<String, Object> item = (Map<String, Object>) list.get(i);
                ps.setObject(1, item.get("num"));
                ps.setObject(2, item.get("product_id"));
                ps.setObject(3, storageId);
            }

            public int getBatchSize() {
                return list.size();
            }
        });
    }

    public void confirmReceive(List<Object> list, String log, Long storageId, Long userId, String order_id) throws CustomException {
        String sql = "update storage_order set status_ = ?,`log` = ?,receive_id=? where id = ? and status_ = ?";
        int update = jdbcTemplate.update(sql, StorageStatus.ORDER_COMPLETE, log, userId, order_id, StorageStatus.ORDER_SEND);
        if (update == 0) throw new CustomException("操作失败");
        MySql mySql = new MySql();
        mySql.append("UPDATE `storage_order_item`  ");
        mySql.append("SET receive_num = ?  ");
        mySql.append("WHERE order_id = ? and product_id = ? ");
        int[] ints = jdbcTemplate.batchUpdate(mySql.toString(), new BatchPreparedStatementSetter() {
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                JSONObject item = (JSONObject) list.get(i);
                ps.setObject(1, item.get("num"));
                ps.setObject(2, order_id);
                ps.setObject(3, item.get("product_id"));
            }

            public int getBatchSize() {
                return list.size();
            }
        });
    }

    public List<Map<String, Object>> receiveList(Pager<Map<String, Object>> pager, Long storage_id) throws CustomException {
        MySql mySql = new MySql();
        mySql.append("SELECT so.*,u.realname request_name,s.realname send_name  ");
        mySql.append("FROM `storage_order` so,`sys_user` u,`sys_user` s");
        mySql.append("WHERE u.id = so.request_id and s.id = so.send_id and so.storage_id = ?", storage_id);
        mySql.orderBy("status_");
        mySql.limit(pager);

        List<Map<String, Object>> maps = jdbcTemplate.queryForList(mySql.toString(), mySql.getValues());
        return maps;
    }


    /**
     * 删除待发货的订单
     *
     * @param storage_id
     */
    public void deleteOrder(Long storage_id) {
        String del = "DELETE s,i FROM storage_order s INNER JOIN storage_order_item i ON s.id = i.order_id WHERE s.storage_id = ? and s.`status_` = ?";
        jdbcTemplate.update(del, storage_id, StorageStatus.ORDER_REQUEST);
    }

    public void createOrder(Long storage_id, Long userId, String dataJson) {
        JSONArray products = JSON.parseArray(dataJson);
        //去除为0 的商品
        Iterator<Object> iterator = products.iterator();
        while (iterator.hasNext()) {
            JSONObject next = (JSONObject) iterator.next();
            if (next.getIntValue("num") == 0) {
                iterator.remove();
            }
        }
        if (products.size() == 0) return;

        Long order_id = Tools.generatorId();
        //默认状态是 装车，所以没设定
        jdbcTemplate.update("INSERT INTO `storage_order`(`id`,`storage_id` , `request_id`) VALUES(?,?,?)", order_id, storage_id, userId);

        String inSql = "INSERT INTO `storage_order_item`(`order_id`,`product_id`, `request_num`) VALUES(?,?,?);";
        int[] ints = jdbcTemplate.batchUpdate(inSql, new BatchPreparedStatementSetter() {
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Map<String, Object> item = (Map<String, Object>) products.get(i);
                ps.setObject(1, order_id);
                ps.setObject(2, item.get("product_id"));
                ps.setObject(3, item.get("num"));
            }

            public int getBatchSize() {
                return products.size();
            }
        });
    }


    /**
     * 发送订单给  没用了
     *
     * @param dataJson  参数  product_id ,  补给数量 num
     * @param storageId
     */
    public void sendStorageOrder(String dataJson, Long storageId) {
        JSONArray list = JSON.parseArray(dataJson);

        String update = "UPDATE `storage_product` set request_num = ? where storage_id = ? and product_id = ?";

        int[] isOk = jdbcTemplate.batchUpdate(update, new BatchPreparedStatementSetter() {
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                JSONObject item = (JSONObject) list.get(i);
                ps.setObject(1, item.get("num"));
                ps.setObject(2, storageId);
                ps.setObject(3, item.get("product_id"));
            }

            public int getBatchSize() {
                return list.size();
            }
        });


        JSONArray insert = new JSONArray();
        for (int i = 0; i < isOk.length; i++) {
            if (isOk[i] == 0) {
                insert.add(list.get(i));
            }
        }

        if (insert.size() > 0) {
            String insql = "insert into storage_product (storage_id,product_id,request_num) values(?,?,?)";
            int[] ints = jdbcTemplate.batchUpdate(insql, new BatchPreparedStatementSetter() {
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    JSONObject item = (JSONObject) insert.get(i);
                    ps.setObject(1, storageId);
                    ps.setObject(2, item.get("product_id"));
                    ps.setObject(3, item.get("num"));
                }

                public int getBatchSize() {
                    return list.size();
                }
            });
        }

    }
}
