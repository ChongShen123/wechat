package com.xsdkj.wechat.ex;

import com.xsdkj.wechat.common.ResultCodeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author tiankong
 * @date 2019/12/18 11:30
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserNotInGroupException extends ServiceException {
    private static final long serialVersionUID = 9148273598414419416L;

    public UserNotInGroupException() {
        code = ResultCodeEnum.USER_NOT_IN_GROUP;
    }
}
