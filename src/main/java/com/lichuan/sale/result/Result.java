package com.lichuan.sale.result;

import com.alibaba.fastjson.JSON;

public abstract class Result {

    private Code code = Code.ERROR;

    private String message;

    public int getCode() {
        return code.getStatus();
    }

    public void setCode(Code code) {
        this.code = code;
    }

    public String getMessage() {
        return message != null ? message : code.getMessage();
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setMessageOfSuccess(String message) {
        this.code = Code.SUCCESS;
        this.message = message;
    }

    public void setMessageOfSuccess() {
        this.code = Code.SUCCESS;
    }

    public void setMessageOfError() {
        this.code = Code.ERROR;
    }

    public void setMessageOfError(String message) {
        this.code = Code.ERROR;
        this.message = message;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

}
