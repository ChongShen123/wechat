package com.xsdkj.wechat.ex;

import com.xsdkj.wechat.common.ResultCodeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author tiankong
 * @date 2020/1/4 14:07
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SystemException extends ServiceException {
    private static final long serialVersionUID = 2775807767036173195L;

    public SystemException() {
        code = ResultCodeEnum.SYSTEM_EXCEPTION;
    }
}
