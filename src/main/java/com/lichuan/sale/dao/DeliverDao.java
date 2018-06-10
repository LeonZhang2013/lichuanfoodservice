package com.lichuan.sale.dao;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lichuan.sale.configurer.StorageStatus;
import com.lichuan.sale.core.CustomException;
import com.lichuan.sale.model.ProductStatus;
import com.lichuan.sale.tools.Tools;
import com.lichuan.sale.tools.sqltools.MySql;
import com.lichuan.sale.tools.sqltools.Pager;
import com.lichuan.sale.tools.sqltools.SQLTools;
import constant.OrderStatus;
import org.springframework.data.domain.Page;
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


    public List<Map<String, Object>> getDeliverProduct(Long userId) {
        MySql mySql = new MySql();
        mySql.append("SELECT a.*,b.num from");
        mySql.append("(SELECT product_image,product_unit,`product_id`,`product_name`, SUM(`buy_num`-`give_num`) buy_num from `order_item` i");
        mySql.append("WHERE proxy_id in (SELECT u.id FROM  user u,user p WHERE p.storage_id = u.storage_id and p.`id` = ? )");
        mySql.append(" and `status_` = ? GROUP BY `product_id`)  a");
        mySql.append("left join");
        mySql.append("(SELECT `product_id`,num FROM `user` u,`storage_product` s");
        mySql.append("WHERE u.storage_id = s.`storage_id` and u.id = ? ) b");
        mySql.append("on a.product_id= b.product_id");

        List<Map<String, Object>> maps = jdbcTemplate.queryForList(mySql.toString(), userId, OrderStatus.WAIT_RECEIVE.getStatus(), userId);
        return maps;
    }

    public List<Map<String, Object>> getDeliverOrderList(Long userId, String key) throws CustomException {
        MySql mySql = new MySql();
        mySql.append("SELECT o.id order_id ,a.name,a.city,a.address,a.mobile,o.create_time,o.update_time");
        mySql.append("FROM `order_item` i,`order_` o,`user_address` a");
        mySql.append("WHERE o.id = i.order_id and a.id = o.address_id and  i.status_ = 1 ");
        mySql.append("and i.`proxy_id` = ?", userId);
        String keys = SQLTools.FuzzyKey(key);
        mySql.notNullAppend("and (a.address like ? or a.name like ?)", keys, keys);
        mySql.append("GROUP BY `order_id` ORDER BY a.id");
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
        jdbcTemplate.update("UPDATE `order_` set `update_time` = now() WHERE `id` = ? ",order_id);

        mySql.append("UPDATE `order_item` set `give_num` = ? , status_=? WHERE `order_id` = ? and `product_id` = ?");
        int[] ints = jdbcTemplate.batchUpdate(mySql.toString(), new BatchPreparedStatementSetter() {
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                JSONObject item = (JSONObject) list.get(i);
                int deliver_num = item.getIntValue("deliver_num");
                int buy_num = item.getIntValue("buy_num");
                ps.setObject(1, deliver_num);
                ps.setObject(2, deliver_num >= buy_num ? OrderStatus.COMPLETE.getStatus() : OrderStatus.WAIT_RECEIVE.getStatus());
                ps.setObject(3, order_id);
                ps.setObject(4, item.get("product_id"));
            }

            public int getBatchSize() {
                return list.size();
            }
        });

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
     * @param storageId  传空代表获取所有
     * @return
     * @throws CustomException
     */
    public List<Map<String, Object>> getUserOrderNum(Long storageId) throws CustomException {
        MySql mySql = new MySql();
        mySql.append("SELECT `product_id`,`product_name`, product_image,SUM(`buy_num`-`give_num`) order_num,product_unit unit from `order_item` i");
        mySql.append("WHERE `status_` = ? ", OrderStatus.WAIT_RECEIVE.getStatus());
        mySql.notNullAppend("and proxy_id in (SELECT id FROM  user WHERE storage_id = ? )", storageId);
        mySql.append("GROUP BY `product_id`");
        return jdbcTemplate.queryForList(mySql.toString(), mySql.getValues());
    }

    //获取 公司发给仓库，待接收商品 和 和补仓信息
    public List<Map<String, Object>> getStorageProductNum(Long storageId) throws CustomException {
        MySql mySql = new MySql();
        mySql.append("SELECT sp.product_id,p.name product_name,p.main_image product_image,sp.num");
        mySql.append("FROM storage_product sp,product p WHERE p.id = sp.product_id and sp.storage_id = ?", storageId);
        List<Map<String, Object>> storage = jdbcTemplate.queryForList(mySql.toString(), storageId);
        return storage;
    }

    public void updateStorageNum(List<Object> list, Long storageId) throws CustomException {
        String update = "UPDATE `storage_product` set `num` = `num`+? WHERE `product_id` = ? and `storage_id` = ?";
        int[] oks = updateStorageNum(update, list, storageId);
        List<Object> insert = new ArrayList<>();
        for(int i=0; i<oks.length; i++) {
            if(oks[i]==0) insert.add(list.get(i));
        }
        if(insert.size()>0){
            String sql = "insert into storage_product (num,product_id,storage_id) values (?,?,?)";
            int[] ints = updateStorageNum(sql, insert, storageId);
            for(int i=0; i<ints.length; i++) {
                if(ints[i]==0) throw  new CustomException("插入失败");
            }
        }
    }

    public int[] updateStorageNum(String sql,List<Object> list, Long storageId) {
        return jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Map<String,Object> item = (Map<String, Object>) list.get(i);
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
        if(update == 0) throw new CustomException("操作失败");
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
        mySql.append("SELECT so.*,u.realname request_name,s.realname send_name FROM `storage_order` so,`user` u,user s ");
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
        while (iterator.hasNext()){
            JSONObject next = (JSONObject) iterator.next();
            if(next.getIntValue("num") == 0){
                iterator.remove();
            }
        }
        if(products.size() == 0) return;

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
