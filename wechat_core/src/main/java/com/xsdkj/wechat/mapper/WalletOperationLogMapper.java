package com.xsdkj.wechat.mapper;

import com.xsdkj.wechat.entity.wallet.WalletOperationLog;import org.apache.ibatis.annotations.Param;

/**
 * @author tiankong
 * @date 2020/1/10 15:53
 */
public interface WalletOperationLogMapper {
    int deleteByPrimaryKey(Integer id);


    int insertSelective(WalletOperationLog record);

    WalletOperationLog selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(WalletOperationLog record);

    int updateByPrimaryKey(WalletOperationLog record);

    /**
     * 保存用户充值或提现记录
     *
     * @param log      记录
     * @param tableNum 表id 用户id取模10获得
     * @return count
     */
    int insert(@Param("log") WalletOperationLog log, @Param("tableNum") int tableNum);
}
