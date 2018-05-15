package com.lichuan.sale.service;

import com.lichuan.sale.dao.CommonDao;
import com.lichuan.sale.dao.RoleDao;
import com.lichuan.sale.dao.UserDao;
import com.lichuan.sale.dao.VerCodeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.annotation.Resource;

public class BaseService  {

    @Resource
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
}
