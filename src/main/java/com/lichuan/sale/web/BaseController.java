package com.lichuan.sale.web;

import com.lichuan.sale.model.User;
import com.lichuan.sale.service.AliYunService;
import com.lichuan.sale.service.CommonService;
import com.lichuan.sale.service.ProductService;
import com.lichuan.sale.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

public class BaseController {

    @Autowired
    ProductService productService;

    @Autowired
    UserService userService;

    @Autowired
    protected AliYunService aliYunService;

    @Autowired
    protected CommonService commonService;

    public User getUser(){
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        return (User) requestAttributes.getAttribute("user", 0);
    }

    protected Long getUserId(){
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        User user = (User) requestAttributes.getAttribute("user", 0);
        return null == user?null:user.getId();
    }
}
