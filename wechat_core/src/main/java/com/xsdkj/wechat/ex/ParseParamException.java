package com.xsdkj.wechat.ex;

import com.xsdkj.wechat.common.IErrorCode;
import com.xsdkj.wechat.common.ResultCodeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author tiankong
 * @date 2019/12/15 15:46
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ParseParamException extends RuntimeException {
    private static final long serialVersionUID = 7783894835679566772L;
    private IErrorCode code = ResultCodeEnum.VALIDATE_FAILED;
    public ParseParamException() {
    }
}
