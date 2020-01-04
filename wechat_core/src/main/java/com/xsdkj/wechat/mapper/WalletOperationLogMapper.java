package com.xsdkj.wechat.mapper;

import com.xsdkj.wechat.entity.wallet.WalletOperationLog;
import org.apache.ibatis.annotations.Param;

/**
 * @author tiankong
 * @date 2020/1/3 16:05
 */
public interface WalletOperationLogMapper {
    int deleteByPrimaryKey(Integer id);

    /**
     * 保存用户充值或提现记录
     *
     * @param log      记录
     * @param tableNum 表id 用户id取模10获得
     * @return count
     */
    int insert(@Param("log") WalletOperationLog log, @Param("tableNum") int tableNum);

    int insertSelective(WalletOperationLog record);

    WalletOperationLog selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(WalletOperationLog record);

    int updateByPrimaryKey(WalletOperationLog record);
}
