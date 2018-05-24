package com.lichuan.sale.model;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

@Table(name = "order_item")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "product_id")
    private Long productId;

    /**
     * 支付类型,1-在线支付
     */
    @Column(name = "payment_type")
    private Integer paymentType;

    /**
     * 运费
     */
    @Column(name = "sub_freight")
    private BigDecimal subFreight;

    /**
     * 商品总价（没加运费）
     */
    @Column(name = "sub_price")
    private BigDecimal subPrice;

    /**
     * 订单状态 0 取消  1 贷付款 2 已付款 3 已发货 4 已完成。
     */
    @Column(name = "order_status")
    private Integer orderStatus;

    /**
     * 订购数量
     */
    @Column(name = "buy_num")
    private Integer buyNum;

    /**
     * 已配送数量
     */
    @Column(name = "give_num")
    private Integer giveNum;

    /**
     * 客户订单号
     */
    @Column(name = "order_id")
    private Long orderId;

    @Column(name = "proxy_id")
    private Long proxyId;

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
     * @return product_id
     */
    public Long getProductId() {
        return productId;
    }

    /**
     * @param productId
     */
    public void setProductId(Long productId) {
        this.productId = productId;
    }

    /**
     * 获取支付类型,1-在线支付
     *
     * @return payment_type - 支付类型,1-在线支付
     */
    public Integer getPaymentType() {
        return paymentType;
    }

    /**
     * 设置支付类型,1-在线支付
     *
     * @param paymentType 支付类型,1-在线支付
     */
    public void setPaymentType(Integer paymentType) {
        this.paymentType = paymentType;
    }

    /**
     * 获取运费
     *
     * @return sub_freight - 运费
     */
    public BigDecimal getSubFreight() {
        return subFreight;
    }

    /**
     * 设置运费
     *
     * @param subFreight 运费
     */
    public void setSubFreight(BigDecimal subFreight) {
        this.subFreight = subFreight;
    }

    /**
     * 获取商品总价（没加运费）
     *
     * @return sub_price - 商品总价（没加运费）
     */
    public BigDecimal getSubPrice() {
        return subPrice;
    }

    /**
     * 设置商品总价（没加运费）
     *
     * @param subPrice 商品总价（没加运费）
     */
    public void setSubPrice(BigDecimal subPrice) {
        this.subPrice = subPrice;
    }

    /**
     * 获取订单状态 0 取消  1 贷付款 2 已付款 3 已发货 4 已完成。
     *
     * @return order_status - 订单状态 0 取消  1 贷付款 2 已付款 3 已发货 4 已完成。
     */
    public Integer getOrderStatus() {
        return orderStatus;
    }

    /**
     * 设置订单状态 0 取消  1 贷付款 2 已付款 3 已发货 4 已完成。
     *
     * @param orderStatus 订单状态 0 取消  1 贷付款 2 已付款 3 已发货 4 已完成。
     */
    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    /**
     * 获取订购数量
     *
     * @return buy_num - 订购数量
     */
    public Integer getBuyNum() {
        return buyNum;
    }

    /**
     * 设置订购数量
     *
     * @param buyNum 订购数量
     */
    public void setBuyNum(Integer buyNum) {
        this.buyNum = buyNum;
    }

    /**
     * 获取已配送数量
     *
     * @return give_num - 已配送数量
     */
    public Integer getGiveNum() {
        return giveNum;
    }

    /**
     * 设置已配送数量
     *
     * @param giveNum 已配送数量
     */
    public void setGiveNum(Integer giveNum) {
        this.giveNum = giveNum;
    }

    /**
     * 获取客户订单号
     *
     * @return order_id - 客户订单号
     */
    public Long getOrderId() {
        return orderId;
    }

    /**
     * 设置客户订单号
     *
     * @param orderId 客户订单号
     */
    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    /**
     * @return proxy_id
     */
    public Long getProxyId() {
        return proxyId;
    }

    /**
     * @param proxyId
     */
    public void setProxyId(Long proxyId) {
        this.proxyId = proxyId;
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