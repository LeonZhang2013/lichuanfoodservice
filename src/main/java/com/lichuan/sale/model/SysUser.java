package com.lichuan.sale.model;

import java.io.Serializable;

public class SysUser implements Serializable {

    private Long id;
    private String icon;
    private String username;
    private String password;
    private String nickname;
    private String mobile;
    private String token;
    private String realname;
    private Integer sex;
    private Integer status_;
    private Long parent_id;
    private Long role_id;
    private String role_name;
    private Long address_id;
    private Integer is_multi;
    private Long proxy_id;//推荐人
    private Long storage_id;
    private String referee;//推荐人

    public String getReferee() {
        return referee;
    }

    public void setReferee(String referee) {
        this.referee = referee;
    }

    public Long getProxy_id() {
        return proxy_id;
    }

    public void setProxy_id(Long proxy_id) {
        this.proxy_id = proxy_id;
    }


    public String getRole_name() {
        return role_name;
    }

    public void setRole_name(String role_name) {
        this.role_name = role_name;
    }

    public Long getStorage_id() {
        return storage_id;
    }

    public void setStorage_id(Long storage_id) {
        this.storage_id = storage_id;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Integer getStatus_() {
        return status_;
    }

    public void setStatus_(Integer status_) {
        this.status_ = status_;
    }

    public Long getParent_id() {
        return parent_id;
    }

    public void setParent_id(Long parent_id) {
        this.parent_id = parent_id;
    }

    public Long getRole_id() {
        return role_id;
    }

    public void setRole_id(Long role_id) {
        this.role_id = role_id;
    }

    public Long getAddress_id() {
        return address_id;
    }

    public void setAddress_id(Long address_id) {
        this.address_id = address_id;
    }

    public Integer getIs_multi() {
        return is_multi;
    }

    public void setIs_multi(Integer is_multi) {
        this.is_multi = is_multi;
    }
}