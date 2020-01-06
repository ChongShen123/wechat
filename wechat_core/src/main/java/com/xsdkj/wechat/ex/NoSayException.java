package com.xsdkj.wechat.ex;

import com.xsdkj.wechat.common.ResultCodeEnum;

/**
 * @author tiankong
 * @date 2019/12/28 15:00
 */
public class NoSayException extends ServiceException {

    private static final long serialVersionUID = 5374286127255663426L;

    public NoSayException() {
        code = ResultCodeEnum.NO_SAY_EXCEPTION;
    }
}
