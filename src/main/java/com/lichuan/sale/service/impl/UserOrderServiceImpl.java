package com.lichuan.sale.service.impl;

import com.lichuan.sale.dao.UserOrderMapper;
import com.lichuan.sale.model.UserOrder;
import com.lichuan.sale.service.UserOrderService;
import com.lichuan.sale.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by leonZhang on 2018/05/13.
 */
@Service
@Transactional
public class UserOrderServiceImpl extends AbstractService<UserOrder> implements UserOrderService {
    @Resource
    private UserOrderMapper userOrderMapper;

}
