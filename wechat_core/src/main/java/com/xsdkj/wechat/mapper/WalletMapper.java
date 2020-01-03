package com.xsdkj.wechat.mapper;

import com.xsdkj.wechat.entity.wallet.Wallet;

/**
 * @author tiankong
 * @date 2020/1/3 9:41
 */
public interface WalletMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Wallet record);

    int insertSelective(Wallet record);

    Wallet selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Wallet record);

    int updateByPrimaryKey(Wallet record);
}
