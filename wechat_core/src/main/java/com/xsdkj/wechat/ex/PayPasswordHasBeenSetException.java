package com.xsdkj.wechat.ex;

import com.xsdkj.wechat.common.IErrorCode;
import com.xsdkj.wechat.common.ResultCodeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 支付密码已被设置
 *
 * @author tiankong
 * @date 2020/1/4 18:10
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PayPasswordHasBeenSetException extends ServiceException {
    private static final long serialVersionUID = 7195338590842339854L;
    private IErrorCode code = ResultCodeEnum.PAY_PASSWORD_HAS_BEEN_SET;
}
