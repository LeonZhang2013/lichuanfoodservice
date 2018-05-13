package com.lichuan.sale.model;

import javax.persistence.*;

public class Unit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    /**
     * 和吨的单位换算
     */
    private Double calc;

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
     * 获取和吨的单位换算
     *
     * @return calc - 和吨的单位换算
     */
    public Double getCalc() {
        return calc;
    }

    /**
     * 设置和吨的单位换算
     *
     * @param calc 和吨的单位换算
     */
    public void setCalc(Double calc) {
        this.calc = calc;
    }
}