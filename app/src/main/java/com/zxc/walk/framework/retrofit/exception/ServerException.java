package com.zxc.walk.framework.retrofit.exception;

/**
 * @author xyu
 * @date 2018/9/18
 * Describe:
 */
public class ServerException extends RuntimeException {
    private String code;
    private String msg;

    public ServerException(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
