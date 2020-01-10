package com.xsdkj.wechat.ex;

import com.xsdkj.wechat.common.ResultCodeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author tiankong
 * @date 2020/1/7 14:10
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AlreadyFriendException extends ServiceException {
    private static final long serialVersionUID = 2491769735096256889L;

    public AlreadyFriendException() {
        code = ResultCodeEnum.ALREADY_FRIEND_EXCEPTION;
    }
}
