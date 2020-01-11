package com.xsdkj.wechat.ex;

import com.xsdkj.wechat.common.ResultCodeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author tiankong
 * @date 2020/1/11 12:25
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TimeoutException extends ServiceException {

    private static final long serialVersionUID = 7703134718381345966L;

    public TimeoutException() {
        code = ResultCodeEnum.TIMEOUT_EXCEPTION;
    }
}
