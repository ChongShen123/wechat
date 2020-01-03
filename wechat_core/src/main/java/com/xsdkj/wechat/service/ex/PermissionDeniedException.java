package com.xsdkj.wechat.service.ex;

import com.xsdkj.wechat.common.IErrorCode;
import com.xsdkj.wechat.common.ResultCodeEnum;
import com.xsdkj.wechat.service.ex.ServiceException;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author tiankong
 * @date 2019/12/26 18:47
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PermissionDeniedException extends ServiceException {
    private static final long serialVersionUID = 2032976030274369402L;
    private IErrorCode code = ResultCodeEnum.FORBIDDEN;

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
