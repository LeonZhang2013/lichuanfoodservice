package com.lichuan.sale.dao;

import com.lichuan.sale.core.CustomException;
import com.lichuan.sale.model.Product;
import com.lichuan.sale.tools.SqlInfo;
import com.lichuan.sale.tools.sqltools.MySql;
import com.lichuan.sale.tools.sqltools.Pager;
import com.lichuan.sale.tools.sqltools.SQLTools;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class ProductDao extends BaseDao {
    public List<Map<String, Object>> getProductList(Pager<Map<String, Object>> pager, String categoryId, String keyword, Integer status, Integer promotion) throws CustomException {
        MySql sql = new MySql("SELECT p.*,c.name category_name from product p, category c WHERE c.id = p.category_id ");
        sql.append(" and c.status_ = 1 ");
        sql.notNullAppend(" and p.category_id = ? ", categoryId);
        sql.notNullAppend(" and p.status_ = ? ", status);
        keyword = SQLTools.FuzzyKey(keyword);
        sql.notNullAppend(" and (p.name like ? or p.subtitle like ?) ", keyword, keyword);
        sql.notNullAppend(" and p.promotion = ? ", promotion);
        sql.orderByDesc("p.id");
        sql.limit(pager);
        List<Map<String, Object>> maps = jdbcTemplate.queryForList(sql.toString(), sql.getValues());
        return maps;
    }

    public List<Map<String, Object>> getUnit() {
        String sql = "select * from unit";
        List<Map<String, Object>> maps = jdbcTemplate.queryForList(sql);

        return maps;
    }

    public List<Map<String, Object>> getCategory(Integer status) {
        MySql mySql = new MySql();
        mySql.append("SELECT * FROM `category`");
        mySql.notNullAppend("WHERE `status_` = ?", status);
        List<Map<String, Object>> maps = jdbcTemplate.queryForList(mySql.toString(), mySql.getValues());
        return maps;
    }

    public int updateProduct(Product product) {
        SqlInfo sqlInfo = new SQLTools().getUpdateById(product, "product", product.getId());
        return jdbcTemplate.update(sqlInfo.getSql(), sqlInfo.getValues());
    }

    public int addProduct(Product product) {
        SqlInfo sqlInfo = new SQLTools().getInsertSQL(product, "product");
        System.out.println(sqlInfo.getSql());
        return jdbcTemplate.update(sqlInfo.getSql(), sqlInfo.getValues());
    }

    public List<Map<String, Object>> getBannerList() {
        String sql = "select * from product where promotion = 1 and status_ = 1  order by `update_time` DESC  limit 0,5";
        List<Map<String, Object>> maps = jdbcTemplate.queryForList(sql);
        return maps;
    }

    public int changeStatus(String id, String status) {
        String sql = "update product set status_ = ? where id=?";
        return jdbcTemplate.update(sql, status, id);
    }

    public int updateCategoryStatus(Long categoryId, Long status) {
        String sql = "update `category` set status_ = ? where id = ?";
        return jdbcTemplate.update(sql, status, categoryId);
    }

    public int addCategory(Long parentId, String name) {
        String sql = "insert into category (parent_id,name) values (?,?)";
        return jdbcTemplate.update(sql, parentId, name);
    }
}
