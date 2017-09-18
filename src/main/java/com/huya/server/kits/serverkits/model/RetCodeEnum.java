package com.huya.server.kits.serverkits.model;

/**
 * Created by Kevin[wangzhuo@yy.com] on 2017/8/27.
 */
public enum RetCodeEnum {
    SUCCESS(200, "success"),
    INTERNAL_ERROR(500, "Server internal error"),
    DATA_ERROR(501, "Data operation failed"),

    NO_DATA_ERROR(741, "The server does not have this data"),
    WRONG_SERVER(1001, "Server ip is wrong"),

    PARAM_ILLEGAL(401, "Parameter is not legal"),
    API_NOTFOUND(404, "API does not exist"),
    TIME_OUT(408, "Request timed out");


    private int code;

    private String msg;

    RetCodeEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

}
