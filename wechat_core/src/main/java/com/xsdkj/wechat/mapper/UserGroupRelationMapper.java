package com.xsdkj.wechat.mapper;

import com.xsdkj.wechat.entity.user.UserGroupRelation;

/**
 * @author tiankong
 * @date 2019/12/28 16:09
 */
public interface UserGroupRelationMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserGroupRelation record);

    int insertSelective(UserGroupRelation record);

    UserGroupRelation selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserGroupRelation record);

    int updateByPrimaryKey(UserGroupRelation record);
}
