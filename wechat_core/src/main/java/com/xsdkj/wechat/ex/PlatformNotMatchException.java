package com.xsdkj.wechat.ex;

import com.xsdkj.wechat.common.ResultCodeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author tiankong
 * @date 2020/1/5 11:37
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PlatformNotMatchException extends ServiceException {
    private static final long serialVersionUID = -201779434177194165L;

    public PlatformNotMatchException() {
        code = ResultCodeEnum.PLATFORM_NOT_MATCH;
    }
}
