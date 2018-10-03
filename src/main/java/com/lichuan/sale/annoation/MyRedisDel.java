package com.lichuan.sale.annoation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface MyRedisDel {

    String value() ;

//    第几个参数作为删除key
    int key() default 0;


}