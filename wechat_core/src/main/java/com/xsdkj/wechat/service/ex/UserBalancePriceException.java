package com.xsdkj.wechat.service.ex;

import com.xsdkj.wechat.common.IErrorCode;
import com.xsdkj.wechat.common.ResultCodeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author tiankong
 * @date 2020/1/3 19:03
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserBalancePriceException extends ServiceException {
    private static final long serialVersionUID = 613954332968981856L;
    private IErrorCode code = ResultCodeEnum.USER_WALLET_BALANCE_EXCEPTION;
}
