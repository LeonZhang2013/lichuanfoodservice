package com.lichuan.sale.model;

import javax.persistence.*;

public class Area {
    /**
     * 地域编码
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 地域名称
     */
    private String name;

    /**
     * 上级地域编码
     */
    @Column(name = "parent_id")
    private Long parentId;

    /**
     * 地址
     */
    private String abbr;

    /**
     * 层级
     */
    private Integer level;

    private Long enable;

    /**
     * 获取地域编码
     *
     * @return id - 地域编码
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置地域编码
     *
     * @param id 地域编码
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取地域名称
     *
     * @return name - 地域名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置地域名称
     *
     * @param name 地域名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取上级地域编码
     *
     * @return parent_id - 上级地域编码
     */
    public Long getParentId() {
        return parentId;
    }

    /**
     * 设置上级地域编码
     *
     * @param parentId 上级地域编码
     */
    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    /**
     * 获取地址
     *
     * @return abbr - 地址
     */
    public String getAbbr() {
        return abbr;
    }

    /**
     * 设置地址
     *
     * @param abbr 地址
     */
    public void setAbbr(String abbr) {
        this.abbr = abbr;
    }

    /**
     * 获取层级
     *
     * @return level - 层级
     */
    public Integer getLevel() {
        return level;
    }

    /**
     * 设置层级
     *
     * @param level 层级
     */
    public void setLevel(Integer level) {
        this.level = level;
    }

    /**
     * @return enable
     */
    public Long getEnable() {
        return enable;
    }

    /**
     * @param enable
     */
    public void setEnable(Long enable) {
        this.enable = enable;
    }
}