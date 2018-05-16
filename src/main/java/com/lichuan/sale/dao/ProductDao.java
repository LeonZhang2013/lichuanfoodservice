package com.lichuan.sale.dao;

import com.lichuan.sale.model.Product;
import com.lichuan.sale.tools.sqltools.Pager;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class ProductDao extends BaseDao{
    public List<Map<String,Object>> getProductList(Pager<Map<String, Object>> pager, String categoryId, String keyword, Integer islist, Integer promotion) {
        return null;
    }

    public List<Map<String,Object>> getUnit() {
        return null;
    }

    public List<Map<String,Object>> getCategory(Pager<Map<String, Object>> pager, String key, String categoryId, Integer islist) {
        return null;
    }

    public int updateSupplier(Long id) {
        return 1;
    }

    public int addProduct(Product product) {
        return 1;
    }
}
