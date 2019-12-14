package com.cxkj.wechat.mapper;

import com.cxkj.wechat.entity.Role;

/**
 * @author tiankong
 * @date 2019/12/11 11:39
 */
public interface RoleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Role record);

    int insertSelective(Role record);

    Role selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Role record);

    int updateByPrimaryKey(Role record);
}