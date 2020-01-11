package com.xsdkj.wechat.mapper;

import com.xsdkj.wechat.entity.wallet.WalletTransferLog;import org.apache.ibatis.annotations.Param;

/**
 * @author tiankong
 * @date 2020/1/10 15:53
 */
public interface WalletTransferLogMapper {
    int deleteByPrimaryKey(Integer id);

    int insertSelective(WalletTransferLog record);

    WalletTransferLog selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(WalletTransferLog record);

    int updateByPrimaryKey(WalletTransferLog record);

    int insert(@Param("log") WalletTransferLog log, @Param("tableNum") int tableNum);
}
