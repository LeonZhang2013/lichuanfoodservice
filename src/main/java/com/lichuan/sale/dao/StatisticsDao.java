package com.lichuan.sale.dao;

import com.lichuan.sale.configurer.RoleConstant;
import com.lichuan.sale.core.CustomException;
import com.lichuan.sale.tools.sqltools.MySql;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class StatisticsDao extends BaseDao {

    public List<Map<String, Object>> getTimeData(String year, String proxy_id, String category_id, boolean isFright, boolean isUnit) throws CustomException {
        MySql mySql = new MySql();
        if (year != null) {
            mySql.append("SELECT MONTH(`create_time`) title,");
        } else {
            mySql.append("SELECT YEAR(`create_time`) id,YEAR(`create_time`) title,");
        }

        appendData(mySql, isFright, isUnit);
        mySql.append(" from order_item i,`order_` o where o.id = i.order_id and o.status_ > 0 ");
        mySql.notNullAppend("and proxy_id = ?", proxy_id);
        mySql.notNullAppend("and category_id = ?", category_id);

        if (year != null) {
            mySql.append("and YEAR(`create_time`) = ? group by MONTH(`create_time`)", year);
        } else {
            mySql.append("group by YEAR(`create_time`)");
        }

        List<Map<String, Object>> maps = jdbcTemplate.queryForList(mySql.toString(), mySql.getValues());
        return maps;
    }


    public List<Map<String, Object>> getStorageData(String category_id, String startTime, String endTime, boolean isFright, boolean isUnit) throws CustomException {
        MySql mySql = new MySql();
        mySql.append("SELECT s.id,s.name title,");
        appendData(mySql, isFright, isUnit);
        mySql.append("FROM `storage` s,`order_item` i,order_ o,sys_user u ");
        mySql.append("where s.id = u.storage_id and o.id = i.order_id and o.proxy_id = u.id  and o.status_ > 0");
        mySql.append("and create_time between  ? and ?", startTime, endTime);
        mySql.notNullAppend("and category_id = ?", category_id);
        mySql.append("GROUP BY s.id");

        List<Map<String, Object>> maps = jdbcTemplate.queryForList(mySql.toString(), mySql.getValues());
        return maps;
    }

    public List<Map<String, Object>> getProxyData(String category_id, String startTime, String endTime, boolean isFright, boolean isUnit) throws CustomException {
        MySql mySql = new MySql();
        mySql.append("SELECT u.id,u.realname title,");
        appendData(mySql, isFright, isUnit);
        mySql.append("FROM sys_user u LEFT JOIN `order_` o on  u.id = o.proxy_id ");
        mySql.append("LEFT JOIN `order_item` i on o.id = i.order_id and create_time ");
        mySql.append("between  ? and ?", startTime, endTime);
        mySql.append(" WHERE  u.`role_id`= ?", RoleConstant.SALE_ID);
        mySql.notNullAppend(" and category_id = ? ", category_id);
        mySql.append("GROUP BY u.id");
        mySql.orderByDesc("data");
        List<Map<String, Object>> maps = jdbcTemplate.queryForList(mySql.toString(), mySql.getValues());
        return maps;
    }

    public List<Map<String, Object>> getUserData(String id, String category_id, String startTime, String endTime, boolean isFright, boolean isUnit) throws CustomException {
        MySql mySql = new MySql();
        mySql.append("SELECT u.realname title,");
        appendData(mySql, isFright, isUnit);
        mySql.append("FROM `order_` o,`order_item` i,user u");
        mySql.append("where o.id = i.order_id and u.id = o.user_id ");
        mySql.append("and o.status_ > 0 and o.proxy_id = ?", id);
        mySql.append("and create_time between  ? and ?", startTime, endTime);
        mySql.notNullAppend("and category_id = ?", category_id);
        mySql.append("GROUP BY u.id");

        List<Map<String, Object>> maps = jdbcTemplate.queryForList(mySql.toString(), mySql.getValues());
        return maps;
    }

    public void appendData(MySql mySql, boolean isFright, boolean isUnit) {
        if (isUnit) {
            mySql.append("SUM(i.`buy_num`) data,i.product_unit unit ");
        } else {
            if (isFright) {
                mySql.append("SUM(i.`sub_price`+i.`sub_freight`) data ");
            } else {
                mySql.append("SUM(i.`sub_price`) data ");
            }
        }
    }

    public List<Map<String, Object>> getProductData(String storage_id, String startTime, String endTime, boolean isFright, boolean isUnit) throws CustomException {
        MySql mySql = new MySql();
        mySql.append("SELECT p.`name` title,");
        appendData(mySql, isFright, isUnit);
        mySql.append("FROM `product` p LEFT JOIN `order_item` i on p.`id` = i.`product_id`");
        mySql.append("LEFT JOIN `order_` o on o.id = i.`order_id` and o.`status_` >0");
        mySql.notNullAppend("and o.storage_id = ?", storage_id);
        mySql.append("and o.create_time between  ? and ?", startTime, endTime);
        mySql.append("where p.status_ = 1");
        mySql.append("GROUP BY p.id");
        mySql.orderBy("data");
        List<Map<String, Object>> maps = jdbcTemplate.queryForList(mySql.toString(), mySql.getValues());
        return maps;
    }
}
