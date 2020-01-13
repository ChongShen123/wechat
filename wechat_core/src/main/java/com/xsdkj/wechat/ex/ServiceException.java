package com.xsdkj.wechat.ex;

import com.xsdkj.wechat.common.IErrorCode;
import com.xsdkj.wechat.common.ResultCodeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author tiankong
 * @date 2019/12/11 13:42
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ServiceException extends RuntimeException implements Serializable {
    private static final long serialVersionUID = -2879099986352308425L;
    protected IErrorCode code;
    public ServiceException() {
    }

    public ServiceException(IErrorCode code) {
        this.code = code;
    }

    public ServiceException(String message, IErrorCode code) {
        super(message);
        this.code = code;
    }

    public ServiceException(String message, Throwable cause, IErrorCode code) {
        super(message, cause);
        this.code = code;
    }

    public ServiceException(Throwable cause, IErrorCode code) {
        super(cause);
        this.code = code;
    }

    public ServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, IErrorCode code) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.code = code;
    }

}
