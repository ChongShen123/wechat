package com.xsdkj.wechat.mapper;

import com.xsdkj.wechat.entity.user.UserOperationLog;
import org.apache.ibatis.annotations.Param;

/**
 * @author tiankong
 * @date 2020/1/3 12:50
 */
public interface UserOperationLogMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(@Param("log") UserOperationLog log);

    int insertSelective(UserOperationLog record);

    UserOperationLog selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserOperationLog record);

    int updateByPrimaryKey(UserOperationLog record);
}
