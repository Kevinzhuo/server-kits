package com.huya.server.kits.serverkits.exception;

/**
 * Created by Kevin[wangzhuo@yy.com] on 2017/8/27.
 */
public class NetworkException extends Exception {

    private static final long serialVersionUID = 2824044062991835381L;

    private Integer httpCode = null;

    public NetworkException(Throwable cause) {
        super(cause);
    }

    public NetworkException(Integer httpCode) {
        this.httpCode = httpCode;
    }

    public NetworkException(Integer httpCode, String message, Throwable cause) {
        super(message, cause);
        this.httpCode = httpCode;
    }

    public Integer getHttpCode() {
        return this.httpCode;
    }

    public String getMessage() {
        return String.format("httpCode:%d", this.httpCode == null ? "" : this.httpCode);
    }

    public String getLocalizedMessage() {
        return this.getMessage();
    }

}
