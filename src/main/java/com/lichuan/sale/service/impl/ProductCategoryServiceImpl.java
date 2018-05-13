package com.lichuan.sale.service.impl;

import com.lichuan.sale.dao.ProductCategoryMapper;
import com.lichuan.sale.model.ProductCategory;
import com.lichuan.sale.service.ProductCategoryService;
import com.lichuan.sale.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by leonZhang on 2018/05/13.
 */
@Service
@Transactional
public class ProductCategoryServiceImpl extends AbstractService<ProductCategory> implements ProductCategoryService {
    @Resource
    private ProductCategoryMapper productCategoryMapper;

}
