package com.lichuan.sale.service;

import com.lichuan.sale.dao.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.annotation.Resource;

public class BaseService  {

    @Autowired
    StatisticsDao statisticsDao;

    @Autowired
    OrderDao orderDao;

    @Autowired
    DeliverDao deliverDao;

    @Autowired
    ShopCartDao shopCartDao;

    @Autowired
    StorageDao storageDao;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    public
    UserDao userDao;

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
