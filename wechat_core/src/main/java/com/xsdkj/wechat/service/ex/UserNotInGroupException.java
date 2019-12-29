package com.xsdkj.wechat.service.ex;

/**
 * @author tiankong
 * @date 2019/12/18 11:30
 */
public class UserNotInGroupException extends RuntimeException {
    public UserNotInGroupException() {
    }

    public UserNotInGroupException(String message) {
        super(message);
    }

    public UserNotInGroupException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserNotInGroupException(Throwable cause) {
        super(cause);
    }

    public UserNotInGroupException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
