package com.xsdkj.wechat.ex;

import com.xsdkj.wechat.common.ResultCodeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author tiankong
 * @date 2020/1/6 10:57
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BeyondRetroactiveException extends ServiceException {
    private static final long serialVersionUID = -2633738333874570162L;

    public BeyondRetroactiveException() {
        code = ResultCodeEnum.RETROACTIVE_COUNT_BEYOND;
    }
}
