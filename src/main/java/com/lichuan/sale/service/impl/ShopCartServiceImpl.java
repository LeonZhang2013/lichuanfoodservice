package com.lichuan.sale.service.impl;

import com.lichuan.sale.dao.ShopCartMapper;
import com.lichuan.sale.model.ShopCart;
import com.lichuan.sale.service.ShopCartService;
import com.lichuan.sale.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by leonZhang on 2018/05/13.
 */
@Service
@Transactional
public class ShopCartServiceImpl extends AbstractService<ShopCart> implements ShopCartService {
    @Resource
    private ShopCartMapper shopCartMapper;

}
