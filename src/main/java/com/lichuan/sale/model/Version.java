package com.lichuan.sale.model;

import javax.persistence.*;

public class Version {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String url;

    private Boolean deprecate;

    private Integer code;

    /**
     * 类型（1、用户端2、管理端）
     */
    private Integer type;

    @Column(name = "package_name")
    private String packageName;

    private String description;

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
     * @return url
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param url
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * @return deprecate
     */
    public Boolean getDeprecate() {
        return deprecate;
    }

    /**
     * @param deprecate
     */
    public void setDeprecate(Boolean deprecate) {
        this.deprecate = deprecate;
    }

    /**
     * @return code
     */
    public Integer getCode() {
        return code;
    }

    /**
     * @param code
     */
    public void setCode(Integer code) {
        this.code = code;
    }

    /**
     * 获取类型（1、用户端2、管理端）
     *
     * @return type - 类型（1、用户端2、管理端）
     */
    public Integer getType() {
        return type;
    }

    /**
     * 设置类型（1、用户端2、管理端）
     *
     * @param type 类型（1、用户端2、管理端）
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * @return package_name
     */
    public String getPackageName() {
        return packageName;
    }

    /**
     * @param packageName
     */
    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    /**
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }
}