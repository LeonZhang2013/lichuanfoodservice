package com.lichuan.sale.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "event_log")
public class EventLog {
    /**
     * 自增ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 发送人id
     */
    @Column(name = "send_id")
    private Long sendId;

    /**
     * 发送内容
     */
    private String content;

    /**
     * 1、新消息、2、紧急、3、未处理、4、已处理
     */
    private Integer status;

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
     * 标题
     */
    private String title;

    /**
     * 处理方式和结果
     */
    private String result;

    /**
     * 获取自增ID
     *
     * @return id - 自增ID
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置自增ID
     *
     * @param id 自增ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取发送人id
     *
     * @return send_id - 发送人id
     */
    public Long getSendId() {
        return sendId;
    }

    /**
     * 设置发送人id
     *
     * @param sendId 发送人id
     */
    public void setSendId(Long sendId) {
        this.sendId = sendId;
    }

    /**
     * 获取发送内容
     *
     * @return content - 发送内容
     */
    public String getContent() {
        return content;
    }

    /**
     * 设置发送内容
     *
     * @param content 发送内容
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * 获取1、新消息、2、紧急、3、未处理、4、已处理
     *
     * @return status - 1、新消息、2、紧急、3、未处理、4、已处理
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置1、新消息、2、紧急、3、未处理、4、已处理
     *
     * @param status 1、新消息、2、紧急、3、未处理、4、已处理
     */
    public void setStatus(Integer status) {
        this.status = status;
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
     * 获取标题
     *
     * @return title - 标题
     */
    public String getTitle() {
        return title;
    }

    /**
     * 设置标题
     *
     * @param title 标题
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * 获取处理方式和结果
     *
     * @return result - 处理方式和结果
     */
    public String getResult() {
        return result;
    }

    /**
     * 设置处理方式和结果
     *
     * @param result 处理方式和结果
     */
    public void setResult(String result) {
        this.result = result;
    }
}