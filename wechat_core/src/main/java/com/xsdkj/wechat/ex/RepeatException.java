package com.xsdkj.wechat.ex;

import com.xsdkj.wechat.common.ResultCodeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author tiankong
 * @date 2020/1/7 13:29
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RepeatException extends ServiceException {
    private static final long serialVersionUID = 6864255643541655430L;

    public RepeatException() {
        code = ResultCodeEnum.REPEAT_EXCEPTION;
    }
}
