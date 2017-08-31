package com.huya.server.kits.serverkits.model;

/**
 * Created by Kevin[wangzhuo@yy.com] on 2017/8/27.
 */
public class ResponseModel<E> {

    public int code;
    public String message;
    public E data;

    public ResponseModel() {
    }

    public int getCode() {
        return this.code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public E getData() {
        return this.data;
    }

    public void setData(E data) {
        this.data = data;
    }

    public String toString() {
        return String.format("{code:%d,message:%s,data:%s}", this.code, this.message, this.data);
    }

}
