package com.zxc.walk.framework.retrofit.entity;


import com.zxc.walk.framework.retrofit.constants.ResultCode;

/**
 * @author xyu
 * @date 2018/9/18
 * Describe:
 */
public class Result<T> {
    private String code = ResultCode.CODE_SUCCESS;
    private String innercode = ResultCode.CODE_SUCCESS;
    private T message;
    private int status = -1;
    private String wrongmessage;


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getWrongmessage() {
        return wrongmessage;
    }

    public void setWrongmessage(String wrongmessage) {
        this.wrongmessage = wrongmessage;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getInnercode() {
        return innercode;
    }

    public void setInnercode(String innercode) {
        this.innercode = innercode;
    }

    public T getMessage() {
        return message;
    }

    public void setMessage(T message) {
        this.message = message;
    }
}
