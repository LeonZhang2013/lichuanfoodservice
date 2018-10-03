package com.lichuan.sale.annoation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface MyRedis {

//    用fa xxxx#{index}
    String value() ;

    long expire() default -1;



}