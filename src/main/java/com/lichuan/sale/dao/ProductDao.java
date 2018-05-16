package com.lichuan.sale.dao;

import com.lichuan.sale.model.Product;
import com.lichuan.sale.tools.SqlInfo;
import com.lichuan.sale.tools.sqltools.Pager;
import com.lichuan.sale.tools.sqltools.SQLTools;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class ProductDao extends BaseDao{
    public List<Map<String,Object>> getProductList(Pager<Map<String, Object>> pager, String categoryId, String keyword, Integer islist, Integer promotion) {
        return null;
    }

    public List<Map<String, Object>> getUnit() {
        String sql = "select * from unit";
        List<Map<String, Object>> maps = jdbcTemplate.queryForList(sql);
        return maps;
    }
    public List<Map<String,Object>> getCategory(Pager<Map<String, Object>> pager, String key, String categoryId, Integer islist) {
        return null;
    }

    public int updateProduct(Product product) {
        SqlInfo sqlInfo = new SQLTools().getUpdateById(product, "product",  product.getId());
        return jdbcTemplate.update(sqlInfo.getSql(), sqlInfo.getValues());
    }
    public int addProduct(Product product) {
        SqlInfo sqlInfo = new SQLTools().getInsertSQL(product, "product");
        System.out.println(sqlInfo.getSql());
        return jdbcTemplate.update(sqlInfo.getSql(), sqlInfo.getValues());
    }

}
