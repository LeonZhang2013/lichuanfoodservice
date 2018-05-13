package com.lichuan.sale.model;

import javax.persistence.*;

public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String password;

    private String nickname;

    private String mobile;

    private String token;

    /**
     * 1：正常，2：冻结
     */
    private Integer status;

    @Column(name = "parent_id")
    private Long parentId;

    @Column(name = "role_id")
    private Long roleId;

    /**
     * 默认地址
     */
    @Column(name = "address_id")
    private String addressId;

    /**
     * 1：可多用户
0：不可多用户
     */
    @Column(name = "is_multi")
    private Integer isMulti;

    @Column(name = "percent_price")
    private Float percentPrice;

    /**
     * @return id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return nickname
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * @param nickname
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * @return mobile
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * @param mobile
     */
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    /**
     * @return token
     */
    public String getToken() {
        return token;
    }

    /**
     * @param token
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * 获取1：正常，2：冻结
     *
     * @return status - 1：正常，2：冻结
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置1：正常，2：冻结
     *
     * @param status 1：正常，2：冻结
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * @return parent_id
     */
    public Long getParentId() {
        return parentId;
    }

    /**
     * @param parentId
     */
    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    /**
     * @return role_id
     */
    public Long getRoleId() {
        return roleId;
    }

    /**
     * @param roleId
     */
    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    /**
     * 获取默认地址
     *
     * @return address_id - 默认地址
     */
    public String getAddressId() {
        return addressId;
    }

    /**
     * 设置默认地址
     *
     * @param addressId 默认地址
     */
    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    /**
     * 获取1：可多用户
0：不可多用户
     *
     * @return is_multi - 1：可多用户
0：不可多用户
     */
    public Integer getIsMulti() {
        return isMulti;
    }

    /**
     * 设置1：可多用户
0：不可多用户
     *
     * @param isMulti 1：可多用户
0：不可多用户
     */
    public void setIsMulti(Integer isMulti) {
        this.isMulti = isMulti;
    }

    /**
     * @return percent_price
     */
    public Float getPercentPrice() {
        return percentPrice;
    }

    /**
     * @param percentPrice
     */
    public void setPercentPrice(Float percentPrice) {
        this.percentPrice = percentPrice;
    }
}