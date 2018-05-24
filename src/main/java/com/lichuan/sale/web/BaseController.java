package com.lichuan.sale.web;

import com.lichuan.sale.model.User;
import com.lichuan.sale.service.*;
import com.lichuan.sale.service.wx.WxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

public class BaseController {

    @Autowired
    public  OrderService orderService;

    @Autowired
    public ProductService productService;

    @Autowired
    public UserService userService;

    @Autowired
    public DeliverService deliverService;

    @Autowired
    public AliYunService aliYunService;

    @Autowired
    public CommonService commonService;

    @Autowired
    public ShopCartService shopCartService;

    @Autowired
    public WxService wxService;

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
