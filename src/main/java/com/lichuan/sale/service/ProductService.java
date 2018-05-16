package com.lichuan.sale.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.lichuan.sale.core.CustomException;
import com.lichuan.sale.model.Product;
import com.lichuan.sale.model.User;
import com.lichuan.sale.tools.Arith;
import com.lichuan.sale.tools.StringUtils;
import com.lichuan.sale.tools.Tools;
import com.lichuan.sale.tools.sqltools.Pager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
public class ProductService extends BaseService {



    public List<Map<String,Object>> getProductList(Pager<Map<String, Object>> pager, String categoryId, String keyword, Integer islist, Integer promotion) {
        List<Map<String, Object>> productList = productDao.getProductList(pager, categoryId, keyword, islist, promotion);
        return productList;
    }

    public List<Map<String,Object>> getUnits() {
        return productDao.getUnit();
    }

    public List<Map<String,Object>> getCategory(Pager<Map<String, Object>> pager, String key, String categoryId, Integer islist) {
        return productDao.getCategory(pager,key,categoryId,islist);
    }

    public boolean updateProduct(Product product) {
        int effect = productDao.updateSupplier(product.getId());
        return effect >0;
    }


    public boolean addProduct(Product product) {
        Long productId = Tools.generatorId();
        product.setId(productId);
        int effect = productDao.addProduct(product);
        return effect >0;
    }









}
