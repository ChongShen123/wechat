package com.xsdkj.wechat.netty.ex;

/**
 * @author tiankong
 * @date 2019/12/15 15:46
 */
public class ParseParamException extends RuntimeException {
    public ParseParamException() {
    }

    public ParseParamException(String message) {
        super(message);
    }

    public ParseParamException(String message, Throwable cause) {
        super(message, cause);
    }

    public ParseParamException(Throwable cause) {
        super(cause);
    }

    public ParseParamException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
