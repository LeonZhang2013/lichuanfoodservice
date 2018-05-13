package com.lichuan.sale.service.impl;

import com.lichuan.sale.dao.UserAddressMapper;
import com.lichuan.sale.model.UserAddress;
import com.lichuan.sale.service.UserAddressService;
import com.lichuan.sale.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by leonZhang on 2018/05/13.
 */
@Service
@Transactional
public class UserAddressServiceImpl extends AbstractService<UserAddress> implements UserAddressService {
    @Resource
    private UserAddressMapper userAddressMapper;

}
