package com.xsdkj.wechat.mapper;

import com.xsdkj.wechat.entity.wallet.WalletOperationLog;

/**
 * @author tiankong
 * @date 2020/1/3 16:03
 */
public interface WalletOperationLogMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(WalletOperationLog record);

    int insertSelective(WalletOperationLog record);

    WalletOperationLog selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(WalletOperationLog record);

    int updateByPrimaryKey(WalletOperationLog record);
}
