package com.xsdkj.wechat.mapper;

import com.xsdkj.wechat.entity.chat.GroupNoSay;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author tiankong
 * @date 2019/12/28 15:54
 */
public interface GroupNoSayMapper {

    GroupNoSay findOneByUidAndGroupId(@Param("uid") Integer uid, @Param("groupId") Integer groupId);

    int deleteByPrimaryKey(Integer id);

    int insert(GroupNoSay record);

    int insertSelective(GroupNoSay record);

    GroupNoSay selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(GroupNoSay record);

    int updateByPrimaryKey(GroupNoSay record);

    List<GroupNoSay> selectByAll(GroupNoSay groupNoSay);

    void save(@Param("uid") Integer uid, @Param("groupId") Integer groupId, @Param("times") Long times);

    void updateByUidAndGid(@Param("uid") Integer uid, @Param("groupId") Integer groupId, @Param("times") Long times);
}
