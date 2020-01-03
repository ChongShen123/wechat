package com.xsdkj.wechat.mapper;

import com.xsdkj.wechat.entity.wallet.WalletPriceLog;

/**
 * @author tiankong
 * @date 2020/1/3 9:42
 */
public interface WalletPriceLogMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(WalletPriceLog record);

    int insertSelective(WalletPriceLog record);

    WalletPriceLog selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(WalletPriceLog record);

    int updateByPrimaryKey(WalletPriceLog record);
}