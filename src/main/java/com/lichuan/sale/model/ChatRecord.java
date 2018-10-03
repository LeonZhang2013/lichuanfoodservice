package com.lichuan.sale.model;

import java.util.Date;

public class ChatRecord {

    private long id;
    private long user_id;
    private String user_icon;
    private long sys_user_id;
    private String sys_user_icon;
    private String message;
    private Date create_time;
    private int type;
    private int state;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public String getUser_icon() {
        return user_icon;
    }

    public void setUser_icon(String user_icon) {
        this.user_icon = user_icon;
    }

    public long getSys_user_id() {
        return sys_user_id;
    }

    public void setSys_user_id(long sys_user_id) {
        this.sys_user_id = sys_user_id;
    }

    public String getSys_user_icon() {
        return sys_user_icon;
    }

    public void setSys_user_icon(String sys_user_icon) {
        this.sys_user_icon = sys_user_icon;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }
}
