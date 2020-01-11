package com.xsdkj.wechat.mapper;

import com.xsdkj.wechat.entity.wallet.Wallet;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

/**
 * @author tiankong
 * @date 2020/1/4 18:08
 */
public interface WalletMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Wallet record);

    int insertSelective(Wallet record);

    Wallet selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Wallet record);

    int updateByPrimaryKey(Wallet record);

    Wallet getOneByUid(@Param("uid") Integer uid);

    /**
     * 修改用户钱包余额
     *
     * @param uid   用户id
     * @param price 金额
     * @return count
     */
    int updateWalletPrice(@Param("uid") Integer uid, @Param("price") BigDecimal price);

    int updateUserWallet(Wallet wallet);

    /**
     * 设置用户支付密码
     *
     * @param uid      用户id
     * @param password 密码
     */
    void updatePayPassword(@Param("uid") Integer uid, @Param("password") String password);
}
