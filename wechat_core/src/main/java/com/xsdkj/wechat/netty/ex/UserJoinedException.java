package com.xsdkj.wechat.netty.ex;

/**
 * @author tiankong
 * @date 2019/12/17 11:33
 */
public class UserJoinedException extends RuntimeException {
    public UserJoinedException() {
    }

    public UserJoinedException(String message) {
        super(message);
    }

    public UserJoinedException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserJoinedException(Throwable cause) {
        super(cause);
    }

    public UserJoinedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
