package com.lichuan.sale.service.impl;

import com.lichuan.sale.dao.UserMapper;
import com.lichuan.sale.model.User;
import com.lichuan.sale.service.UserService;
import com.lichuan.sale.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by leonZhang on 2018/05/13.
 */
@Service
@Transactional
public class UserServiceImpl extends AbstractService<User> implements UserService {
    @Resource
    private UserMapper userMapper;

}
