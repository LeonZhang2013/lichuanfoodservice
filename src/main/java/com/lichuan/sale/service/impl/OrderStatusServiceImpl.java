package com.lichuan.sale.service.impl;

import com.lichuan.sale.dao.OrderStatusMapper;
import com.lichuan.sale.model.OrderStatus;
import com.lichuan.sale.service.OrderStatusService;
import com.lichuan.sale.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by leonZhang on 2018/05/13.
 */
@Service
@Transactional
public class OrderStatusServiceImpl extends AbstractService<OrderStatus> implements OrderStatusService {
    @Resource
    private OrderStatusMapper orderStatusMapper;

}
