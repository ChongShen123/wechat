package com.xsdkj.wechat.ex;

import com.xsdkj.wechat.common.ResultCodeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author tiankong
 * @date 2020/1/5 19:30
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class IllegalOperationException extends ServiceException {
    private static final long serialVersionUID = 6452670334058725441L;

    public IllegalOperationException() {
        code = ResultCodeEnum.ILLEGAL_OPERATION_EXCEPTION;
    }
}
