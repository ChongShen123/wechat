package com.xsdkj.wechat.mapper;


import com.xsdkj.wechat.entity.user.UserLoginLog;

/**
 * @author tiankong
 * @date 2019/12/11 11:39
 */
public interface UserLoginLogMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserLoginLog record);

    int insertSelective(UserLoginLog record);

    UserLoginLog selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserLoginLog record);

    int updateByPrimaryKey(UserLoginLog record);
}
