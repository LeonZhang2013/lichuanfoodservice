package com.lichuan.sale.dao;

import com.lichuan.sale.core.CustomException;
import com.lichuan.sale.tools.sqltools.MySql;
import org.springframework.stereotype.Repository;

import java.time.Year;
import java.util.List;
import java.util.Map;

@Repository
public class StatisticsDao extends BaseDao {

    public List<Map<String, Object>> getTimeData(String year, String proxy_id, String product_id, boolean isFright, boolean isUnit) throws CustomException {
        MySql mySql = new MySql();
        if(year!=null){
            mySql.append("SELECT MONTH(`create_time`) title,");
        }else{
            mySql.append("SELECT YEAR(`create_time`) id,YEAR(`create_time`) title,");
        }

        appendData(mySql,isFright,isUnit);
        mySql.append(" from order_item i where 1=1  ");
        mySql.notNullAppend("and proxy_id = ?",proxy_id);
        mySql.notNullAppend("and product_id = ?",product_id);

        if(year!=null){
            mySql.append("and YEAR(`create_time`) = ? group by MONTH(`create_time`)",year);
        }else{
            mySql.append("group by YEAR(`create_time`)");
        }

        List<Map<String, Object>> maps = jdbcTemplate.queryForList(mySql.toString(), mySql.getValues());
        return maps;
    }


    public List<Map<String,Object>> getStorageData(String product_id, String startTime, String endTime, boolean isFright, boolean isUnit) throws CustomException {
        MySql mySql = new MySql();
        mySql.append("SELECT s.id,s.name title,");
        appendData(mySql,isFright,isUnit);
        mySql.append("FROM `storage` s,`order_item` i,user u  ");
        mySql.append("where s.id = u.storage_id and i.proxy_id = u.id ");
        mySql.append("and create_time between  ? and ?",startTime,endTime);
        mySql.notNullAppend("and product_id = ?",product_id);
        mySql.append("GROUP BY s.id");
        List<Map<String, Object>> maps = jdbcTemplate.queryForList(mySql.toString(), mySql.getValues());
        return maps;
    }

    public List<Map<String,Object>> getProxyData(String id, String product_id, String startTime, String endTime, boolean isFright, boolean isUnit) throws CustomException {
        MySql mySql = new MySql();
        mySql.append("SELECT u.id,u.realname title,");
        appendData(mySql,isFright,isUnit);
        mySql.append("FROM `order_item` i,user u");
        mySql.append("where u.id = i.proxy_id and u.storage_id = ?",id);
        mySql.append("and create_time between  ? and ?",startTime,endTime);
        mySql.notNullAppend("and product_id = ?",product_id);
        mySql.append("GROUP BY i.proxy_id");
        List<Map<String, Object>> maps = jdbcTemplate.queryForList(mySql.toString(), mySql.getValues());
        return maps;
    }

    public List<Map<String,Object>> getUserData(String id, String product_id, String startTime, String endTime, boolean isFright, boolean isUnit) throws CustomException {
        MySql mySql = new MySql();
        mySql.append("SELECT u.realname title,");
        appendData(mySql,isFright,isUnit);
        mySql.append("FROM `order_item` i,user u");
        mySql.append("where u.id = i.user_id and i.proxy_id = ?",id);
        mySql.append("and create_time between  ? and ?",startTime,endTime);
        mySql.notNullAppend("and product_id = ?",product_id);
        mySql.append("GROUP BY i.user_id");
        List<Map<String, Object>> maps = jdbcTemplate.queryForList(mySql.toString(), mySql.getValues());
        return maps;
    }

    public void appendData(MySql mySql,boolean isFright,boolean isUnit){
        if(isUnit){
            mySql.append("SUM(i.`buy_num`) data,i.product_unit unit ");
        }else{
            if(isFright){
                mySql.append("SUM(i.`sub_price`+i.`sub_freight`) data ");
            }else{
                mySql.append("SUM(i.`sub_price`) data ");
            }
        }
    }
}
