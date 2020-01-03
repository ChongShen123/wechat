package com.xsdkj.wechat.service.impl;

import com.xsdkj.wechat.entity.wallet.Wallet;
import com.xsdkj.wechat.entity.wallet.WalletPriceLog;
import com.xsdkj.wechat.mapper.WalletMapper;
import com.xsdkj.wechat.mapper.WalletPriceLogMapper;
import com.xsdkj.wechat.service.UserWalletService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author tiankong
 * @date 2020/1/3 12:41
 */
@Service
public class UserWalletServiceImpl implements UserWalletService {
    @Resource
    private WalletMapper walletMapper;
    @Resource
    private WalletPriceLogMapper walletPriceLogMapper;

    @Override
    public void updateUserWallet(Wallet wallet) {
        walletMapper.updateUserWallet(wallet);
    }

    @Override
    public void saveWalletPriceLog(WalletPriceLog log) {
        walletPriceLogMapper.insert(log);
    }
}
