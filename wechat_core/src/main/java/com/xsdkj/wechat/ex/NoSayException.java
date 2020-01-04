package com.xsdkj.wechat.ex;

import com.xsdkj.wechat.common.IErrorCode;

/**
 * @author tiankong
 * @date 2019/12/28 15:00
 */
public class NoSayException extends ServiceException {

    private static final long serialVersionUID = 5374286127255663426L;

    public NoSayException() {
    }

    public NoSayException(IErrorCode code) {
        super(code);
    }

    public NoSayException(String message, IErrorCode code) {
        super(message, code);
    }

    public NoSayException(String message, Throwable cause, IErrorCode code) {
        super(message, cause, code);
    }

    public NoSayException(Throwable cause, IErrorCode code) {
        super(cause, code);
    }

    public NoSayException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, IErrorCode code) {
        super(message, cause, enableSuppression, writableStackTrace, code);
    }
}
