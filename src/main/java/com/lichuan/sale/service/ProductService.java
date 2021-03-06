package com.lichuan.sale.service;

import com.lichuan.sale.core.CustomException;
import com.lichuan.sale.model.Product;
import com.lichuan.sale.tools.SqlInfo;
import com.lichuan.sale.tools.Tools;
import com.lichuan.sale.tools.sqltools.Pager;
import com.lichuan.sale.tools.sqltools.SQLTools;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ProductService extends BaseService {


    public List<Map<String, Object>> getProductList(Pager<Map<String, Object>> pager, String categoryId, String keyword, Integer status, Integer promotion) throws CustomException {
        List<Map<String, Object>> productList = productDao.getProductList(pager, categoryId, keyword, status, promotion);
        return productList;
    }

    public List<Map<String, Object>> getUnits() {
        return productDao.getUnit();
    }

    public List<Map<String, Object>> getCategory(Integer status) {
        return productDao.getCategory(status);
    }

    public int updateProduct(Product product) {
        SqlInfo sqlInfo = new SQLTools().getUpdateById(product, "product", product.getId());
        return jdbcTemplate.update(sqlInfo.getSql(), sqlInfo.getValues());
    }


    public boolean addProduct(Product product) {
        Long productId = Tools.generatorId();
        product.setId(productId);
        int effect = productDao.addProduct(product);
        return effect > 0;
    }


    public List<Map<String, Object>> getBannerList() {
        return productDao.getBannerList();
    }

    public void changeStatus(String id, String status) throws CustomException {
        int effect = productDao.changeStatus(id, status);
        if (effect == 0) throw new CustomException("更新失败更新失败");
    }

    public void updateCategoryStatus(Long categoryId, Long status) throws CustomException {
        int effect = productDao.updateCategoryStatus(categoryId, status);
        if (effect == 0) throw new CustomException("更新失败更新失败");
    }

    public void addCategory(Long parentId, String name) throws CustomException {
        int effect = productDao.addCategory(parentId, name);
        if (effect == 0) throw new CustomException("更新失败更新失败");
    }
}
