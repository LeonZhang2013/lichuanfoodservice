package com.lichuan.sale.service;

import com.lichuan.sale.dao.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.annotation.Resource;

public class BaseService  {

    @Autowired
    OrderDao orderDao;

    @Autowired
    ShopCartDao shopCartDao;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
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
