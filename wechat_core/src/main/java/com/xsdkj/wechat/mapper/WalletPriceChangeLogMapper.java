package com.xsdkj.wechat.mapper;

import com.xsdkj.wechat.entity.wallet.WalletPriceChangeLog;
import org.apache.ibatis.annotations.Param;

/**
 * @author tiankong
 * @date 2020/1/3 18:17
 */
public interface WalletPriceChangeLogMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(@Param("log") WalletPriceChangeLog log, @Param("tableNum") int tableNum);

    int insertSelective(WalletPriceChangeLog record);

    WalletPriceChangeLog selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(WalletPriceChangeLog record);

    int updateByPrimaryKey(WalletPriceChangeLog record);
}
