package com.huya.server.kits.serverkits.exception;

import com.huya.server.kits.serverkits.model.ResponseModel;

/**
 * Created by Kevin[wangzhuo@yy.com] on 2017/8/27.
 */
public class RemoteException extends Exception {

    private static final long serialVersionUID = -7722132615060000021L;

    public ResponseModel responseModel;

    public RemoteException(ResponseModel responseModel) {
        this.responseModel = responseModel;
    }

    public ResponseModel getResponseModel() {
        return this.responseModel;
    }

    public String getMessage() {
        return String.format("responseModel:%s", this.responseModel == null ? "" : this.responseModel.toString());
    }

    public String getLocalizedMessage() {
        return this.getMessage();
    }

}
