package com.xsdkj.wechat.ex;

import com.xsdkj.wechat.common.IErrorCode;
import com.xsdkj.wechat.common.ResultCodeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author tiankong
 * @date 2019/12/15 14:36
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ValidateException extends ServiceException {
    private static final long serialVersionUID = -3480777004762284117L;
    private IErrorCode code = ResultCodeEnum.VALIDATE_FAILED;
}
