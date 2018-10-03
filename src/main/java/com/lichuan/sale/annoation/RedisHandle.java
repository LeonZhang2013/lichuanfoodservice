package com.lichuan.sale.annoation;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Aspect
@Component
public class RedisHandle {

    @Autowired
    RedisTemplate redisTemplate;

    @Around("@annotation(myRedis)")
    public Object aroundMethod(ProceedingJoinPoint pjd, MyRedis myRedis) throws Throwable {

        Object[] args = pjd.getArgs();
        String key = myRedis.value();

        for (int i = 0; i < args.length; i++) {
            String replace = "#{" + i + "}";
            key = key.replace(replace, String.valueOf(args[i]));
        }

        ValueOperations valueOperations = redisTemplate.opsForValue();
        Object value = valueOperations.get(key);

        if (value == null) {
            value = pjd.proceed();
            valueOperations.set(key, value, myRedis.expire(), TimeUnit.SECONDS);
        }
        return value;
    }


    @Around("@annotation(myRedisdel)")
    public Object aroundMethod(ProceedingJoinPoint pjd, MyRedisDel myRedisdel) throws Throwable {
        Object[] args = pjd.getArgs();
        String key = myRedisdel.value();

        for (int i = 0; i < args.length; i++) {
            String replace = "#{" + i + "}";
            key = key.replace(replace, String.valueOf(args[i]));
        }

        ValueOperations valueOperations = redisTemplate.opsForValue();
        Object value = pjd.proceed();
        valueOperations.set(key, "", 1, TimeUnit.SECONDS);//直接设置1秒过期
        return value;
    }

}