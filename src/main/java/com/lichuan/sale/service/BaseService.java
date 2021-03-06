package com.lichuan.sale.service;

import com.lichuan.sale.dao.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.annotation.Resource;

public class BaseService {

    @Autowired
    StatisticsDao statisticsDao;

    @Autowired
    protected
    OrderDao orderDao;

    @Autowired
    DeliverDao deliverDao;

    @Autowired
    public
    ShopCartDao shopCartDao;

    @Autowired
    StorageDao storageDao;

    @Autowired
    public
    JdbcTemplate jdbcTemplate;

    @Autowired
    protected
    UserDao userDao;

    @Autowired
    SysUserDao sysUserDao;

    @Autowired
    RoleDao roleDao;

    @Autowired
    protected AuthService auth;

    @Autowired
    VerCodeDao verCodeDao;

    @Autowired
    CommonDao commonDao;

    @Autowired
    ProductDao productDao;
}
