package com.xsdkj.wechat.service.impl;

import com.xsdkj.wechat.entity.wallet.WalletPriceChangeLog;
import com.xsdkj.wechat.entity.wallet.Wallet;
import com.xsdkj.wechat.entity.wallet.WalletOperationLog;
import com.xsdkj.wechat.mapper.WalletMapper;
import com.xsdkj.wechat.mapper.WalletOperationLogMapper;
import com.xsdkj.wechat.mapper.WalletPriceChangeLogMapper;
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
    private WalletOperationLogMapper walletOperationLogMapper;
    @Resource
    private WalletPriceChangeLogMapper walletPriceChangeLogMapper;

    @Override
    public void updateUserWallet(Wallet wallet) {
        walletMapper.updateUserWallet(wallet);
    }

    @Override
    public void saveWalletOperationLog(WalletOperationLog log) {
        walletOperationLogMapper.insert(log);
    }

    @Override
    public void saveWalletPriceChangeLog(WalletPriceChangeLog walletPriceChangeLog) {
        walletPriceChangeLogMapper.insert(walletPriceChangeLog);
    }
}
