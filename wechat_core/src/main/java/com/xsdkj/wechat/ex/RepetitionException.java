package com.xsdkj.wechat.ex;


import com.xsdkj.wechat.common.ResultCodeEnum;

/**
 * 重复操作异常
 *
 * @author tiankong
 * @date 2019/12/28 18:26
 */
public class RepetitionException extends ServiceException {
    private static final long serialVersionUID = 839376111154887474L;

    public RepetitionException() {
        code = ResultCodeEnum.REPEAT_EXCEPTION;
    }
}
