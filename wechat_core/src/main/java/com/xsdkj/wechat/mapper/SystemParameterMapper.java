package com.xsdkj.wechat.mapper;

import com.xsdkj.wechat.entity.wallet.SystemParameter;

/**
 * @author tiankong
 * @date 2020/1/6 10:38
 */
public interface SystemParameterMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SystemParameter record);

    int insertSelective(SystemParameter record);

    SystemParameter selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SystemParameter record);

    int updateByPrimaryKey(SystemParameter record);
}