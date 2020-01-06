package com.xsdkj.wechat.ex;

import com.xsdkj.wechat.common.ResultCodeEnum;

/**
 * 暂未登录或token已过期
 *
 * @author tiankong
 * @date 2019/12/30 9:18
 */
public class UnAuthorizedException extends ServiceException {
    private static final long serialVersionUID = -3774751405205947911L;

    public UnAuthorizedException() {
        code = ResultCodeEnum.UNAUTHORIZED;
    }
}
