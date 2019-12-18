package com.cxkj.wechat.netty.ex;

/**
 * @author tiankong
 * @date 2019/12/18 11:30
 */
public class UserNotInGroup extends RuntimeException {
    public UserNotInGroup() {
    }

    public UserNotInGroup(String message) {
        super(message);
    }

    public UserNotInGroup(String message, Throwable cause) {
        super(message, cause);
    }

    public UserNotInGroup(Throwable cause) {
        super(cause);
    }

    public UserNotInGroup(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
