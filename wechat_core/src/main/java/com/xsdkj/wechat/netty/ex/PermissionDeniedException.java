package com.xsdkj.wechat.netty.ex;

import com.xsdkj.wechat.common.IErrorCode;
import com.xsdkj.wechat.service.ex.ServiceException;

/**
 * @author tiankong
 * @date 2019/12/26 18:47
 */
public class PermissionDeniedException extends ServiceException {
    public PermissionDeniedException() {
    }

    public PermissionDeniedException(IErrorCode code) {
        super(code);
    }

    public PermissionDeniedException(String message, IErrorCode code) {
        super(message, code);
    }

    public PermissionDeniedException(String message, Throwable cause, IErrorCode code) {
        super(message, cause, code);
    }

    public PermissionDeniedException(Throwable cause, IErrorCode code) {
        super(cause, code);
    }

    public PermissionDeniedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, IErrorCode code) {
        super(message, cause, enableSuppression, writableStackTrace, code);
    }
}
