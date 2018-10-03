package com.lichuan.sale.dao;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.lichuan.sale.configurer.StorageStatus;
import com.lichuan.sale.core.CustomException;
import com.lichuan.sale.model.Storage;
import com.lichuan.sale.tools.SqlInfo;
import com.lichuan.sale.tools.Tools;
import com.lichuan.sale.tools.sqltools.MySql;
import com.lichuan.sale.tools.sqltools.SQLTools;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Repository
public class StorageDao extends BaseDao {


    @Transactional
    public void updateStorage(Storage storage, String proxy_ids) throws CustomException {

        int update = 0;
        String table = "storage";
        if (storage.getId() == null) {
            Long storageId = Tools.generatorId();
            storage.setId(storageId);
            SqlInfo insertSQL = SQLTools.getInsertSQL(storage, table);
            update = jdbcTemplate.update(insertSQL.toString(), insertSQL.getValues());
        } else {
            SqlInfo updateById = SQLTools.getUpdateById(storage, table, storage.getId());
            update = jdbcTemplate.update(updateById.toString(), updateById.getValues());
        }
        if (update == 0) throw new CustomException("操作失败");

        //首先删除库房关联业务员。然后在添加
        jdbcTemplate.update("update sys_user set storage_id = null where storage_id = ?", storage.getId());
        if (proxy_ids != null && proxy_ids.length() > 0) {
            String[] split = proxy_ids.split(",");
            String inSql = "update sys_user set storage_id = ? where id = ?";
            int[] ints = jdbcTemplate.batchUpdate(inSql, new BatchPreparedStatementSetter() {
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    String id = split[i];
                    ps.setObject(1, storage.getId());
                    ps.setObject(2, id);
                }

                public int getBatchSize() {
                    return split.length;
                }
            });
            for (int i = 0; i < ints.length; i++) {
                if (ints[i] == 0) {
                    throw new CustomException("更新用户失败");
                }
            }
        }
    }

    public List<Map<String, Object>> getStorageListHasOrder() {
        String sql = "select s.*,o.id order_id,o.update_time from storage_order o,storage s where o.storage_id = s.id and o.status_ = ? and s.status_ = ?";
        return jdbcTemplate.queryForList(sql, StorageStatus.ORDER_REQUEST, StorageStatus.ENABLE);
    }

    public void updateStorageStatus(Long storage_id, String status) throws CustomException {
        int update = jdbcTemplate.update("update storage set status_ = ? where id = ?", status, storage_id);
        if (update == 0) throw new CustomException("操作失败");
    }


    /**
     * 获取库房总体库存信息
     *
     * @return
     */
    public List<Map<String, Object>> getTotalStorageNum() throws CustomException {
        MySql mySql = new MySql();
        mySql.append("SELECT p.id product_id,p.name product_name,p.main_image product_image,p.unit,SUM(s.num) num");
        mySql.append("FROM `storage_product` s,`product` p,`storage` st ");
        mySql.append("WHERE s.product_id = p.id and st.id = s.storage_id  ");//1 = 库房可用。
        mySql.append("and st.status_ = ? ", StorageStatus.ENABLE);
        mySql.append("GROUP BY s.product_id");
        return jdbcTemplate.queryForList(mySql.toString(), mySql.getValues());
    }

    /**
     * 获取单个等待收货的订单。
     *
     * @return
     * @throws CustomException
     */
    public List<Map<String, Object>> getNotCompleteOrderNum(String storage_id) throws CustomException {
        MySql mySql = new MySql();
        mySql.append("SELECT i.`product_id`,p.name product_name,p.main_image product_image,p.unit");
        mySql.append(",SUM(receive_num) receive_num,SUM(send_num) wait_num");
        mySql.append("FROM `storage_order_item` i,`storage_order` o,product p");
        mySql.append("WHERE o.id = i.`order_id`and p.id = i.product_id ");
        mySql.notNullAppend("and o.status_ = ? ", StorageStatus.ORDER_REQUEST);
        mySql.notNullAppend(" and o.storage_id = ?", storage_id);
        mySql.append("GROUP BY product_id");
        return jdbcTemplate.queryForList(mySql.toString(), mySql.getValues());
    }

    /**
     * 获取请求的仓库订单。
     *
     * @return
     * @throws CustomException
     */
    public List<Map<String, Object>> getStorageOrderReceive(String storage_id) throws CustomException {
        MySql mySql = new MySql();
        mySql.append("SELECT i.`product_id`,p.name product_name,p.main_image product_image,p.unit");
        mySql.append(",SUM(receive_num) receive_num, ");
        mySql.append("FROM `storage_order_item` i,`storage_order` o,product p");
        mySql.append("WHERE o.id = i.`order_id`and p.id = i.product_id ");
        mySql.notNullAppend("and o.status_ = ? ", StorageStatus.ORDER_COMPLETE);
        mySql.notNullAppend(" and o.storage_id = ?", storage_id);
        mySql.append("GROUP BY product_id");
        return jdbcTemplate.queryForList(mySql.toString(), mySql.getValues());
    }

    /**
     * 获取请求的仓库订单。
     *
     * @return
     * @throws CustomException
     */
    public List<Map<String, Object>> getStorageOrderWait(String storage_id) throws CustomException {
        MySql mySql = new MySql();
        mySql.append("SELECT i.`product_id`,p.name product_name,p.main_image product_image,p.unit");
        mySql.append(",SUM(send_num) wait_num ");
        mySql.append("FROM `storage_order_item` i,`storage_order` o,product p");
        mySql.append("WHERE o.id = i.`order_id`and p.id = i.product_id ");
        mySql.notNullAppend("and o.status_ = ? ", StorageStatus.ORDER_SEND);
        mySql.notNullAppend(" and o.storage_id = ?", storage_id);
        mySql.append("GROUP BY product_id");
        return jdbcTemplate.queryForList(mySql.toString(), mySql.getValues());
    }


    /**
     * 获取请求的仓库订单。
     *
     * @return
     * @throws CustomException
     */
    public List<Map<String, Object>> getStorageOrderRequest(String storage_id) throws CustomException {
        MySql mySql = new MySql();
        mySql.append("SELECT i.`product_id`,p.name product_name,p.main_image product_image,p.unit");
        mySql.append(",SUM(request_num) request_num ");
        mySql.append("FROM `storage_order_item` i,`storage_order` o,product p");
        mySql.append("WHERE o.id = i.`order_id`and p.id = i.product_id ");
        mySql.notNullAppend("and o.status_ = ? ", StorageStatus.ORDER_REQUEST);
        mySql.notNullAppend(" and o.storage_id = ?", storage_id);
        mySql.append("GROUP BY product_id");
        return jdbcTemplate.queryForList(mySql.toString(), mySql.getValues());
    }


    /**
     * 获取 单个库房的订单数据
     *
     * @return
     * @throws CustomException
     */
    public List<Map<String, Object>> getRequestOrderNum(String storageId) throws CustomException {
        MySql mySql = new MySql();
        mySql.append("SELECT i.order_id,`product_id`,p.name product_name,p.main_image product_image,p.unit");
        mySql.append(",send_num wait_num,request_num ");
        mySql.append("FROM `storage_order_item` i,`storage_order` o,product p");
        mySql.append("WHERE o.id = i.`order_id` and p.id = i.product_id");
        mySql.append("and o.status_ = ? ", StorageStatus.ORDER_REQUEST);
        mySql.append("and o.storage_id = ? ", storageId);
        mySql.append("GROUP BY product_id");
        return jdbcTemplate.queryForList(mySql.toString(), mySql.getValues());
    }


    /**
     * 获取 汇总库房的订单数据
     *
     * @return
     * @throws CustomException
     */
    public List<Map<String, Object>> getRequestOrderNum() throws CustomException {
        MySql mySql = new MySql();
        mySql.append("SELECT `product_id`,p.name product_name,p.main_image product_image,p.unit");
        mySql.append(",SUM(send_num) wait_num,SUM(request_num) request_num");
        mySql.append("FROM `storage_order_item` i,`storage_order` o,product p");
        mySql.append("WHERE o.id = i.`order_id` and p.id = i.product_id");
        mySql.append("and o.status_ = ? ", StorageStatus.ORDER_REQUEST);
        mySql.append("GROUP BY product_id");
        return jdbcTemplate.queryForList(mySql.toString(), mySql.getValues());
    }

    public List<Map<String, Object>> getStorageOrderInfo(String storage_id) {
        MySql mySql = new MySql();
        mySql.append("SELECT p.id product_id,p.name product_name ,p.main_image product_image,s.num, s.request_num ");
        mySql.append("FROM storage_product s,product p WHERE p.id = s.product_id  and storage_id = ? order by request_num DESC");
        return jdbcTemplate.queryForList(mySql.toString(), storage_id);
    }

    @Transactional
    public void updateOrder(String dataJson) {
        JSONArray products = JSON.parseArray(dataJson);
        String inSql = "update `storage_order_item` set `send_num`= ? where `order_id` = ? and product_id = ?";
        int[] ints = jdbcTemplate.batchUpdate(inSql, new BatchPreparedStatementSetter() {
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Map<String, Object> item = (Map<String, Object>) products.get(i);
                ps.setObject(1, item.get("wait_num"));
                ps.setObject(2, item.get("order_id"));
                ps.setObject(3, item.get("product_id"));
            }

            public int getBatchSize() {
                return products.size();
            }
        });
    }

    public void sendOutCart(Long userId, String storage_id, String cartNo) {
        String sql = "UPDATE `storage_order` SET send_id = ?,status_ = ?,cart_no=? WHERE storage_id = ? and status_ = ?";
        jdbcTemplate.update(sql, userId, StorageStatus.ORDER_SEND, cartNo, storage_id, StorageStatus.ORDER_REQUEST);
    }

    public List<Map<String, Object>> getSalesByStorageId(String storageId) {
        return jdbcTemplate.queryForList("select id,realname,mobile from sys_user where storage_id = ?", storageId);
    }


    public List<Map<String, Object>> getOrderSingleProduct(String product_id) throws CustomException {
        MySql mySql = new MySql();
        mySql.append("SELECT o.id order_id,s.name,SUM(send_num) wait_num,SUM(request_num) request_num");
        mySql.append(",p.name product_name,p.main_image product_image,p.unit,i.product_id");
        mySql.append("FROM `storage_order_item` i,`storage_order` o,`storage` s,product p");
        mySql.append("WHERE o.id = i.`order_id` and s.id = o.storage_id and p.id = i.product_id");
        mySql.append("and o.status_ = ? and s.status_ = ?", StorageStatus.ORDER_REQUEST, StorageStatus.ENABLE);
        mySql.append("and i.`product_id`  = ?", product_id);
        mySql.append("GROUP BY s.id");
        return jdbcTemplate.queryForList(mySql.toString(), mySql.getValues());
    }

    public List<Map<String, Object>> getStorageList() {
        return jdbcTemplate.queryForList("select * from storage");
    }

    public List<Map<String, Object>> getProxyCustomer(String proxy_id) throws CustomException {
        MySql mySql = new MySql();
        mySql.append("SELECT count(id) count");
        mySql.append("FROM user ");
        mySql.append("WHERE proxy_id = ?", proxy_id);
        return jdbcTemplate.queryForList(mySql.toString(), mySql.getValues());
    }

    public long addStorageProxy(String storage_id, String proxy_id) throws CustomException {
        MySql mySql = new MySql();
        mySql.append("UPDATE `sys_user` SET ");
        mySql.append("`storage_id` = ? ", storage_id);
        mySql.append("WHERE `id` = ?", proxy_id);
        return jdbcTemplate.update(mySql.toString(), mySql.getValues());
    }

    public long deleteStorageProxy(String proxy_id) throws CustomException {
        MySql mySql = new MySql();
        mySql.append("UPDATE `sys_user` SET ");
        mySql.append("`storage_id` = ? ", -1);
        mySql.append("WHERE `id` = ?", proxy_id);
        return jdbcTemplate.update(mySql.toString(), mySql.getValues());
    }

    public long changeUserProxy(String user_id, String proxy_id) throws CustomException {
        MySql mySql = new MySql();
        mySql.append("UPDATE `user` SET ");
        mySql.append("`proxy_id` = ? ", proxy_id);
        mySql.append("WHERE `id` = ?", user_id);
        return jdbcTemplate.update(mySql.toString(), mySql.getValues());
    }
}
