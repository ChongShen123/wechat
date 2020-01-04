package com.xsdkj.wechat.mapper;

import com.xsdkj.wechat.entity.wallet.WalletTransferLog;
import org.apache.ibatis.annotations.Param;

/**
 * @author tiankong
 * @date 2020/1/4 14:37
 */
public interface WalletTransferLogMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(@Param("log") WalletTransferLog log, @Param("tableNum") int tableNum);

    int insertSelective(WalletTransferLog record);

    WalletTransferLog selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(WalletTransferLog record);

    int updateByPrimaryKey(WalletTransferLog record);
}
