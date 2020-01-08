package com.xsdkj.wechat.ex;

import com.xsdkj.wechat.common.ResultCodeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author tiankong
 * @date 2020/1/5 11:58
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AlreadySignedInException extends ServiceException {
    private static final long serialVersionUID = 3090641897959601714L;

    public AlreadySignedInException() {
        code = ResultCodeEnum.ALREADY_SIGNED_IN;
    }
}
