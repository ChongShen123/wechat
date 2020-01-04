package com.xsdkj.wechat.service;

import com.xsdkj.wechat.dto.UserPriceOperationDto;
import com.xsdkj.wechat.entity.wallet.Wallet;

import java.math.BigDecimal;

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
     * @param uid  用户id
     * @param type 是否从缓存获取数据
     * @return Wallet
     */
    Wallet getByUid(Integer uid, boolean type);

    /**
     * 用户转账
     *
     * @param userWallet   转账用户
     * @param toUserWallet 接收用户
     * @param price        金额
     * @return 是否成功
     */
    boolean transferAccounts(Wallet userWallet, Wallet toUserWallet, BigDecimal price);

    /**
     * 创建一个钱包
     *
     * @param uid 用户id
     * @return Wallet
     */
    Wallet createNewWallet(Integer uid);

    /**
     * 保存一个钱包
     *
     * @param wallet 钱包
     */
    void save(Wallet wallet);

    /**
     * 更新支付密码
     *
     * @param uid      用户id
     * @param password 密码
     */
    void updatePayPassword(Integer uid, String password);

    /**
     * 修改支付密码
     * @param uid 用户id
     * @param password 新密码
     * @param oldPassword 旧密码
     */
    void resetPayPassword(Integer uid, String password, String oldPassword);
}
