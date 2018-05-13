package com.lichuan.sale.service.impl;

import com.lichuan.sale.dao.ProductMapper;
import com.lichuan.sale.model.Product;
import com.lichuan.sale.service.ProductService;
import com.lichuan.sale.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by leonZhang on 2018/05/13.
 */
@Service
@Transactional
public class ProductServiceImpl extends AbstractService<Product> implements ProductService {
    @Resource
    private ProductMapper productMapper;

}
