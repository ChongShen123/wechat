package com.xsdkj.wechat.ex;


import com.xsdkj.wechat.common.ResultCodeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author tiankong
 * @date 2019/12/25 19:22
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserNotFountException extends ServiceException {
    private static final long serialVersionUID = -2705620325851599542L;
    public UserNotFountException() {
         code = ResultCodeEnum.USER_NOT_FOND;
    }
}
