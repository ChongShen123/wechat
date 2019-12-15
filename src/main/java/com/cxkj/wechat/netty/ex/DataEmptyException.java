package com.cxkj.wechat.netty.ex;

/**
 * @author tiankong
 * @date 2019/12/15 14:58
 */
public class DataEmptyException extends RuntimeException {
    public DataEmptyException() {
        super();
    }

    public DataEmptyException(String message) {
        super(message);
    }

    public DataEmptyException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataEmptyException(Throwable cause) {
        super(cause);
    }

    protected DataEmptyException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
