package com.lichuan.sale.model;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    /**
     * 单价
     */
    private BigDecimal price;

    /**
     * 商品副标题
     */
    private String subtitle;

    /**
     * 产品主图,url相对地址
     */
    @Column(name = "main_image")
    private String mainImage;

    /**
     * 单位
     */
    private String unit;

    /**
     * 库存数量
     */
    private Integer stock;

    /**
     * 状态（1、在售 2、已下架、3、删除）
     */
    private Boolean status;

    /**
     * 分类id,对应mmall_category表的主键
     */
    @Column(name = "category_id")
    private Long categoryId;

    /**
     * 库房基数 * 运费
     */
    private BigDecimal freight;

    private Float weight;

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
     * 图片地址,json格式,扩展用
     */
    @Column(name = "sub_images")
    private String subImages;

    /**
     * 商品详情，json格式，保存图片地址
     */
    private String detail;

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
     * 获取单价
     *
     * @return price - 单价
     */
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * 设置单价
     *
     * @param price 单价
     */
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    /**
     * 获取商品副标题
     *
     * @return subtitle - 商品副标题
     */
    public String getSubtitle() {
        return subtitle;
    }

    /**
     * 设置商品副标题
     *
     * @param subtitle 商品副标题
     */
    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    /**
     * 获取产品主图,url相对地址
     *
     * @return main_image - 产品主图,url相对地址
     */
    public String getMainImage() {
        return mainImage;
    }

    /**
     * 设置产品主图,url相对地址
     *
     * @param mainImage 产品主图,url相对地址
     */
    public void setMainImage(String mainImage) {
        this.mainImage = mainImage;
    }

    /**
     * 获取单位
     *
     * @return unit - 单位
     */
    public String getUnit() {
        return unit;
    }

    /**
     * 设置单位
     *
     * @param unit 单位
     */
    public void setUnit(String unit) {
        this.unit = unit;
    }

    /**
     * 获取库存数量
     *
     * @return stock - 库存数量
     */
    public Integer getStock() {
        return stock;
    }

    /**
     * 设置库存数量
     *
     * @param stock 库存数量
     */
    public void setStock(Integer stock) {
        this.stock = stock;
    }

    /**
     * 获取状态（1、在售 2、已下架、3、删除）
     *
     * @return status - 状态（1、在售 2、已下架、3、删除）
     */
    public Boolean getStatus() {
        return status;
    }

    /**
     * 设置状态（1、在售 2、已下架、3、删除）
     *
     * @param status 状态（1、在售 2、已下架、3、删除）
     */
    public void setStatus(Boolean status) {
        this.status = status;
    }

    /**
     * 获取分类id,对应mmall_category表的主键
     *
     * @return category_id - 分类id,对应mmall_category表的主键
     */
    public Long getCategoryId() {
        return categoryId;
    }

    /**
     * 设置分类id,对应mmall_category表的主键
     *
     * @param categoryId 分类id,对应mmall_category表的主键
     */
    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    /**
     * 获取库房基数 * 运费
     *
     * @return freight - 库房基数 * 运费
     */
    public BigDecimal getFreight() {
        return freight;
    }

    /**
     * 设置库房基数 * 运费
     *
     * @param freight 库房基数 * 运费
     */
    public void setFreight(BigDecimal freight) {
        this.freight = freight;
    }

    /**
     * @return weight
     */
    public Float getWeight() {
        return weight;
    }

    /**
     * @param weight
     */
    public void setWeight(Float weight) {
        this.weight = weight;
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

    /**
     * 获取图片地址,json格式,扩展用
     *
     * @return sub_images - 图片地址,json格式,扩展用
     */
    public String getSubImages() {
        return subImages;
    }

    /**
     * 设置图片地址,json格式,扩展用
     *
     * @param subImages 图片地址,json格式,扩展用
     */
    public void setSubImages(String subImages) {
        this.subImages = subImages;
    }

    /**
     * 获取商品详情，json格式，保存图片地址
     *
     * @return detail - 商品详情，json格式，保存图片地址
     */
    public String getDetail() {
        return detail;
    }

    /**
     * 设置商品详情，json格式，保存图片地址
     *
     * @param detail 商品详情，json格式，保存图片地址
     */
    public void setDetail(String detail) {
        this.detail = detail;
    }
}