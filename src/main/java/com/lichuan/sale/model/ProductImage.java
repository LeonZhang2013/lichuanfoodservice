package com.lichuan.sale.model;

import javax.persistence.*;

@Table(name = "product_image")
public class ProductImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String path;

    /**
     * oss 文件服务器地址
     */
    @Column(name = "oss_path")
    private String ossPath;

    @Column(name = "product_id")
    private Long productId;

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
     * @return path
     */
    public String getPath() {
        return path;
    }

    /**
     * @param path
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * 获取oss 文件服务器地址
     *
     * @return oss_path - oss 文件服务器地址
     */
    public String getOssPath() {
        return ossPath;
    }

    /**
     * 设置oss 文件服务器地址
     *
     * @param ossPath oss 文件服务器地址
     */
    public void setOssPath(String ossPath) {
        this.ossPath = ossPath;
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
}