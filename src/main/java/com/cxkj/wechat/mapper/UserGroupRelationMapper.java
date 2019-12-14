package com.cxkj.wechat.mapper;

import com.cxkj.wechat.entity.UserGroupRelation;

/**
 * @author tiankong
 * @date 2019/12/11 11:39
 */
public interface UserGroupRelationMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserGroupRelation record);

    int insertSelective(UserGroupRelation record);

    UserGroupRelation selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserGroupRelation record);

    int updateByPrimaryKey(UserGroupRelation record);
}