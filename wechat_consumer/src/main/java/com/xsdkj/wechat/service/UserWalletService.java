package com.xsdkj.wechat.service;

import com.xsdkj.wechat.entity.wallet.Wallet;
import com.xsdkj.wechat.entity.wallet.WalletPriceLog;

/**
 * @author tiankong
 * @date 2020/1/3 12:38
 */
public interface UserWalletService {
    /**
     * 修改用户钱包
     *
     * @param wallet 用户钱包
     */
    void updateUserWallet(Wallet wallet);

    /**
     * 保存金额操作流水记录
     *
     * @param log 记录
     */
    void saveWalletPriceLog(WalletPriceLog log);
}
