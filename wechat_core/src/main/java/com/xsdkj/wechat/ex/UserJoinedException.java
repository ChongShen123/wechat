package com.xsdkj.wechat.ex;

import com.xsdkj.wechat.common.ResultCodeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author tiankong
 * @date 2019/12/17 11:33
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserJoinedException extends ServiceException {
    private static final long serialVersionUID = -800835946950313658L;

    public UserJoinedException() {
        code = ResultCodeEnum.USER_JOINED_EXCEPTION;
    }
}
