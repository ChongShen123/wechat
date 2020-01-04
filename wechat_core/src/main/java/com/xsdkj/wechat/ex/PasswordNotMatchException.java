package com.xsdkj.wechat.ex;

import com.xsdkj.wechat.common.ResultCodeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author tiankong
 * @date 2020/1/4 18:42
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PasswordNotMatchException extends ServiceException {
    private static final long serialVersionUID = 2886714512620390783L;

    public PasswordNotMatchException() {
        code = ResultCodeEnum.PASSWORD_NOT_MATCH;
    }
}
