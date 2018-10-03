package com.lichuan.sale.web;

import com.lichuan.sale.annoation.SysContent;
import com.lichuan.sale.model.SysUser;
import com.lichuan.sale.model.User;
import com.lichuan.sale.service.*;
import com.lichuan.sale.service.wx.WxPayService;
import com.lichuan.sale.service.wx.WxService;
import org.springframework.beans.factory.annotation.Autowired;

public class BaseController {

    @Autowired
    StatisticsService statisticsService;

    @Autowired
    public OrderService orderService;

    @Autowired
    AuthService auth;

    @Autowired
    public VipService mVipService;

    @Autowired
    protected
    WxPayService wxPayService;

    @Autowired
    public ProductService productService;

    @Autowired
    public UserService userService;

    @Autowired
    public SysUserService sysUserService;

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

    public User getUser() {
        return SysContent.getUser();
    }

    public SysUser getSysUser() {
        return getUser();
    }

    /**
     * 获取业务有仓库id
     *
     * @return
     */
    protected Long getStorageId() {
        User user = SysContent.getUser();
        return null == user ? null : user.getStorage_id();
    }

    protected Long getUserId() {
        User user = SysContent.getUser();
        return null == user ? null : user.getId();
    }
}
