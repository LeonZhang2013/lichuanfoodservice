package com.lichuan.sale.web;

import com.lichuan.sale.model.User;
import com.lichuan.sale.service.*;
import com.lichuan.sale.service.wx.WxPayService;
import com.lichuan.sale.service.wx.WxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

public class BaseController {

    @Autowired
    StatisticsService statisticsService;

    @Autowired
    public  OrderService orderService;

    @Autowired
    AuthService auth;

    @Autowired
    protected
    WxPayService wxPayService;

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

    @Autowired
    public StorageService storageService;

    public User getUser(){
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        return (User) requestAttributes.getAttribute("user", 0);
    }

    /**
     * 获取业务有仓库id
     * @return
     */
    protected Long getStorageId(){
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        User user = (User) requestAttributes.getAttribute("user", 0);
        return null == user?null:user.getStorage_id();
    }

    protected Long getUserId(){
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        User user = (User) requestAttributes.getAttribute("user", 0);
        return null == user?null:user.getId();
    }
}
