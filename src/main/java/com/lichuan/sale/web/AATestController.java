package com.lichuan.sale.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;


@RestController
@RequestMapping("/test")
public class AATestController {

    @Autowired
    JedisPool jedisPool;

    @RequestMapping("/testRedis")
    public String test(){
        jedisPool.getResource().set("aaa","ccccc");
        return jedisPool.getResource().get("aaa");
    }

}
