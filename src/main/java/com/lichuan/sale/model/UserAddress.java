package com.lichuan.sale.model;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

@Table(name = "user_address")
public class UserAddress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 收货地址或者门店地址
     */
    private String address;

    private String name;

    private String mobile;

    @Column(name = "user_id")
    private Long userId;

    /**
     * 省
     */
    @Column(name = "r_province")
    private String rProvince;

    /**
     * 市
     */
    @Column(name = "r_city")
    private String rCity;

    /**
     * 区/县
     */
    @Column(name = "r_district")
    private String rDistrict;

    /**
     * 详细地址
     */
    @Column(name = "r_address")
    private String rAddress;

    /**
     * 到达目的地，每吨多少钱
     */
    private BigDecimal freight;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private Date updateTime;

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
     * 获取收货地址或者门店地址
     *
     * @return address - 收货地址或者门店地址
     */
    public String getAddress() {
        return address;
    }

    /**
     * 设置收货地址或者门店地址
     *
     * @param address 收货地址或者门店地址
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     */
    public void setName(String name) {
        this.name = name;
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
     * @return user_id
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * @param userId
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * 获取省
     *
     * @return r_province - 省
     */
    public String getrProvince() {
        return rProvince;
    }

    /**
     * 设置省
     *
     * @param rProvince 省
     */
    public void setrProvince(String rProvince) {
        this.rProvince = rProvince;
    }

    /**
     * 获取市
     *
     * @return r_city - 市
     */
    public String getrCity() {
        return rCity;
    }

    /**
     * 设置市
     *
     * @param rCity 市
     */
    public void setrCity(String rCity) {
        this.rCity = rCity;
    }

    /**
     * 获取区/县
     *
     * @return r_district - 区/县
     */
    public String getrDistrict() {
        return rDistrict;
    }

    /**
     * 设置区/县
     *
     * @param rDistrict 区/县
     */
    public void setrDistrict(String rDistrict) {
        this.rDistrict = rDistrict;
    }

    /**
     * 获取详细地址
     *
     * @return r_address - 详细地址
     */
    public String getrAddress() {
        return rAddress;
    }

    /**
     * 设置详细地址
     *
     * @param rAddress 详细地址
     */
    public void setrAddress(String rAddress) {
        this.rAddress = rAddress;
    }

    /**
     * 获取到达目的地，每吨多少钱
     *
     * @return freight - 到达目的地，每吨多少钱
     */
    public BigDecimal getFreight() {
        return freight;
    }

    /**
     * 设置到达目的地，每吨多少钱
     *
     * @param freight 到达目的地，每吨多少钱
     */
    public void setFreight(BigDecimal freight) {
        this.freight = freight;
    }

    /**
     * 获取创建时间
     *
     * @return create_time - 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建时间
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取更新时间
     *
     * @return update_time - 更新时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置更新时间
     *
     * @param updateTime 更新时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}