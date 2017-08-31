package com.huya.server.kits.serverkits.model;

/**
 * Created by Kevin[wangzhuo@yy.com] on 2017/8/27.
 */
public enum RegisterRoleEnum {
    PRODUCER(1, "producer"),
    CONSUMER(2, "consumer");

    private int code;

    private String name;

    RegisterRoleEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

}
