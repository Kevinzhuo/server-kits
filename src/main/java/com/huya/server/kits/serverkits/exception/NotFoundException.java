package com.huya.server.kits.serverkits.exception;

/**
 * Created by Kevin[wangzhuo@yy.com] on 2017/8/27.
 */
public class NotFoundException extends Exception {

    private static final long serialVersionUID = -3248232793636116907L;

    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundException(String message) {
        super(message);
    }
}
