package com.xsdkj.wechat.service.ex;

import com.xsdkj.wechat.common.IErrorCode;
import com.xsdkj.wechat.common.ResultCodeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author tiankong
 * @date 2020/1/3 13:26
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserWalletBalanceException extends ServiceException {
    private static final long serialVersionUID = -617908345520378657L;
    private IErrorCode code = ResultCodeEnum.USER_WALLET_BALANCE_EXCEPTION;
}
