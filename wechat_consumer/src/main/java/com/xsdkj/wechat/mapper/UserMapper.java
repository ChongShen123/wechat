package com.xsdkj.wechat.mapper;

import com.xsdkj.wechat.entity.user.User;

/**
 * @author tiankong
 * @date 2020/1/2 10:32
 */
public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);
}
