package com.lichuan.sale.dao;

import org.springframework.jdbc.core.JdbcTemplate;

import javax.annotation.Resource;

public class BaseDao {

    @Resource
    JdbcTemplate jdbcTemplate;
}
