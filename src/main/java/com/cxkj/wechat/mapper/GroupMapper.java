package com.cxkj.wechat.mapper;

import org.apache.ibatis.annotations.Param;

import com.cxkj.wechat.bo.UserGroup;
import com.cxkj.wechat.entity.Group;

import java.util.List;

/**
 * @author tiankong
 * @date 2019/12/12 11:15
 */
public interface GroupMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Group record);

    int insertSelective(Group record);

    Group selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Group record);

    int updateByPrimaryKey(Group record);

    List<UserGroup> listGroupByUid(Integer uid);

    List<Group> selectByAll(Group group);


}
