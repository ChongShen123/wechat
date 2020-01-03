package com.xsdkj.wechat.mapper;

import com.xsdkj.wechat.entity.wallet.WalletPriceChangeLog;

/**
 * @author tiankong
 * @date 2020/1/3 18:16
 */
public interface WalletPriceChangeLogMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(WalletPriceChangeLog record);

    int insertSelective(WalletPriceChangeLog record);

    WalletPriceChangeLog selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(WalletPriceChangeLog record);

    int updateByPrimaryKey(WalletPriceChangeLog record);
}