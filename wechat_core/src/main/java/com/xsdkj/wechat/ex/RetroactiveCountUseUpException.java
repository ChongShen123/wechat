package com.xsdkj.wechat.ex;

import com.xsdkj.wechat.common.ResultCodeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author tiankong
 * @date 2020/1/6 11:12
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RetroactiveCountUseUpException extends ServiceException {
    private static final long serialVersionUID = 2548637662054472855L;

    public RetroactiveCountUseUpException() {
        code = ResultCodeEnum.RETROACTIVE_COUNT_USE_UP;
    }
}
