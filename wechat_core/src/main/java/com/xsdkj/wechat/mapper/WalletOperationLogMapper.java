package com.xsdkj.wechat.mapper;

import com.xsdkj.wechat.dto.UserPriceOperationLogDto;
import com.xsdkj.wechat.entity.wallet.WalletOperationLog;
import com.xsdkj.wechat.vo.admin.UserOperationLogVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;


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

    /**
     * 充值提现记录
     *
     * @param param    参数
     * @param tableNum
     * @return List
     */
    List<UserOperationLogVo> listUserPriceOperationLog(@Param("param") UserPriceOperationLogDto param, @Param("tableNum") int tableNum);
}
