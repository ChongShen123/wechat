package com.xsdkj.wechat.service.ex;

import com.xsdkj.wechat.common.IErrorCode;

import java.io.Serializable;

/**
 * @author tiankong
 * @date 2019/12/30 14:15
 */
public class FileNotFoundException extends ServiceException implements Serializable {
    private static final long serialVersionUID = -2146716503601972045L;
    private IErrorCode code;

    public FileNotFoundException() {
    }

    public FileNotFoundException(IErrorCode code) {
        this.code = code;
    }

    public FileNotFoundException(IErrorCode code, IErrorCode code1) {
        super(code);
        this.code = code1;
    }

    public FileNotFoundException(String message, IErrorCode code, IErrorCode code1) {
        super(message, code);
        this.code = code1;
    }

    public FileNotFoundException(String message, Throwable cause, IErrorCode code, IErrorCode code1) {
        super(message, cause, code);
        this.code = code1;
    }

    public FileNotFoundException(Throwable cause, IErrorCode code, IErrorCode code1) {
        super(cause, code);
        this.code = code1;
    }

    public FileNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, IErrorCode code, IErrorCode code1) {
        super(message, cause, enableSuppression, writableStackTrace, code);
        this.code = code1;
    }
}
