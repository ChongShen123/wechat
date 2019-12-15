package com.cxkj.wechat.mapper;

import com.cxkj.wechat.entity.Group;
import com.cxkj.wechat.vo.GroupBaseInfoVO;
import com.cxkj.wechat.vo.GroupInfoVO;
import com.cxkj.wechat.vo.ListGroupVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author tiankong
 * @date 2019/12/14 18:51
 */
public interface GroupMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Group record);

    int insertSelective(Group record);

    Group selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Group record);

    int updateByPrimaryKey(Group record);


    List<Group> selectByAll(Group group);

    void insertUserIds(@Param("ids") List<Integer> ids, @Param("groupId") Integer groupId);

    void updateQr(@Param("id") Integer id, @Param("qr") String qr);

    Group getById(@Param("id") Integer id);

    List<ListGroupVO> listGroupByUserId(Integer userId);

    GroupBaseInfoVO getBaseInfo(@Param("groupId") Integer groupId);

    GroupInfoVO getInfo(@Param("groupId") Integer groupId);

}
