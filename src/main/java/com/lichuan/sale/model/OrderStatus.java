package com.lichuan.sale.model;

import javax.persistence.*;

@Table(name = "order_status")
public class OrderStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    /**
     * 按钮显示文字
     */
    @Column(name = "btn_str")
    private String btnStr;

    private Long next;

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
     * 获取按钮显示文字
     *
     * @return btn_str - 按钮显示文字
     */
    public String getBtnStr() {
        return btnStr;
    }

    /**
     * 设置按钮显示文字
     *
     * @param btnStr 按钮显示文字
     */
    public void setBtnStr(String btnStr) {
        this.btnStr = btnStr;
    }

    /**
     * @return next
     */
    public Long getNext() {
        return next;
    }

    /**
     * @param next
     */
    public void setNext(Long next) {
        this.next = next;
    }
}