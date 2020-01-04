package com.xsdkj.wechat.ex;

import com.xsdkj.wechat.common.ResultCodeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author tiankong
 * @date 2020/1/4 19:05
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PayPasswordIsEmptyException extends ServiceException {
    private static final long serialVersionUID = -8939318171417592951L;

    public PayPasswordIsEmptyException() {
        code = ResultCodeEnum.PAY_PASSWORD_IS_EMPTY;
    }
}
