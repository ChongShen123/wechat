package com.xsdkj.wechat.service;

import com.xsdkj.wechat.dto.UserPriceOperationDto;
import com.xsdkj.wechat.entity.wallet.Wallet;

/**
 * @author tiankong
 * @date 2020/1/3 10:30
 */
public interface UserWalletService {
    /**
     * 用户充值或提现
     *
     * @param userPriceOperationDto 参数
     */
    void priceOperation(UserPriceOperationDto userPriceOperationDto);

    /**
     * 查询用户钱包
     *
     * @param uid 用户id
     * @return Wallet
     */
    Wallet getByUid(Integer uid);
}
