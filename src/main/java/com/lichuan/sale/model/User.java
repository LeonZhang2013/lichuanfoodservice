package com.lichuan.sale.model;


public class User extends SysUser {

    private Vip vip;

    public Vip getVip() {
        return vip;
    }

    public void setVip(Vip vip) {
        this.vip = vip;
    }
}