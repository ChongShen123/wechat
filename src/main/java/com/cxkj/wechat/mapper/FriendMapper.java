package com.cxkj.wechat.mapper;

import com.cxkj.wechat.entity.Friend;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author tiankong
 * @date 2019/12/12 18:53
 */
public interface FriendMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Friend record);

    int insertSelective(Friend record);

    Friend selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Friend record);

    int updateByPrimaryKey(Friend record);


    void saveList(@Param("friends") List<Friend> friends);
}
