package com.lichuan.sale.service.impl;

import com.lichuan.sale.dao.ProductImageMapper;
import com.lichuan.sale.model.ProductImage;
import com.lichuan.sale.service.ProductImageService;
import com.lichuan.sale.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by leonZhang on 2018/05/13.
 */
@Service
@Transactional
public class ProductImageServiceImpl extends AbstractService<ProductImage> implements ProductImageService {
    @Resource
    private ProductImageMapper productImageMapper;

}
