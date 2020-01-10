package com.xsdkj.wechat.ex;

import com.xsdkj.wechat.common.ResultCodeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author tiankong
 * @date 2020/1/6 19:13
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserAlreadyRegister extends ServiceException {
    private static final long serialVersionUID = 2234191872892788145L;

    public UserAlreadyRegister() {
        code = ResultCodeEnum.USER_LOGGED_IN;
    }
}
