package com.lichuan.sale.model;

import java.util.Date;
import javax.persistence.*;

public class Vercode {
    @Id
    private Integer sid;

    /**
     * 电话号码
     */
    private String phone;

    /**
     * 验证码
     */
    private String code;

    @Column(name = "send_time")
    private Date sendTime;

    /**
     * 有效时间
     */
    @Column(name = "expires_in")
    private Date expiresIn;

    /**
     * @return sid
     */
    public Integer getSid() {
        return sid;
    }

    /**
     * @param sid
     */
    public void setSid(Integer sid) {
        this.sid = sid;
    }

    /**
     * 获取电话号码
     *
     * @return phone - 电话号码
     */
    public String getPhone() {
        return phone;
    }

    /**
     * 设置电话号码
     *
     * @param phone 电话号码
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * 获取验证码
     *
     * @return code - 验证码
     */
    public String getCode() {
        return code;
    }

    /**
     * 设置验证码
     *
     * @param code 验证码
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * @return send_time
     */
    public Date getSendTime() {
        return sendTime;
    }

    /**
     * @param sendTime
     */
    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    /**
     * 获取有效时间
     *
     * @return expires_in - 有效时间
     */
    public Date getExpiresIn() {
        return expiresIn;
    }

    /**
     * 设置有效时间
     *
     * @param expiresIn 有效时间
     */
    public void setExpiresIn(Date expiresIn) {
        this.expiresIn = expiresIn;
    }
}