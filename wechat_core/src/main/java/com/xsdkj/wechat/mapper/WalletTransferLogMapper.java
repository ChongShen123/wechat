package com.xsdkj.wechat.mapper;

import com.xsdkj.wechat.entity.wallet.WalletTransferLog;

/**
 * @author tiankong
 * @date 2020/1/3 9:42
 */
public interface WalletTransferLogMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(WalletTransferLog record);

    int insertSelective(WalletTransferLog record);

    WalletTransferLog selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(WalletTransferLog record);

    int updateByPrimaryKey(WalletTransferLog record);
}