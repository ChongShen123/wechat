package com.xsdkj.wechat.mapper;

import com.xsdkj.wechat.entity.user.UserOperationLog;

/**
 * @author tiankong
 * @date 2020/1/3 9:45
 */
public interface UserOperationLogMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserOperationLog record);

    int insertSelective(UserOperationLog record);

    UserOperationLog selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserOperationLog record);

    int updateByPrimaryKey(UserOperationLog record);
}
