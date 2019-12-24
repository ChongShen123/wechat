package com.cxkj.wechat.mapper;

import com.cxkj.wechat.entity.Permission;

/**
 * @author tiankong
 * @date 2019/12/23 13:46
 */
public interface PermissionMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Permission record);

    int insertSelective(Permission record);

    Permission selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Permission record);

    int updateByPrimaryKey(Permission record);
}