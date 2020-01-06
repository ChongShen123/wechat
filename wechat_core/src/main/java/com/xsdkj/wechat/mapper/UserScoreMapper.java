package com.xsdkj.wechat.mapper;

import com.xsdkj.wechat.entity.wallet.UserScore;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * @author tiankong
 * @date 2020/1/5 12:45
 */
public interface UserScoreMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserScore record);

    int insertSelective(UserScore record);

    UserScore selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserScore record);

    int updateByPrimaryKey(UserScore record);

    /**
     * 修改用户积分及连续签到次数
     *
     * @param retroactiveCount 补签次数
     * @param score            积分: 是在已有用户积分上进行加减
     * @param userId           用户id
     * @param successionCount  连续签到天数
     */
    void updateUserScore(@Param("score") Integer score, @Param("userId") Integer userId, @Param("successionCount") Integer successionCount, @Param("retroactiveCount") Integer retroactiveCount);

    /**
     * 更新签到平台签到人数
     *
     * @param count 人数: 在已有数据上进行加减
     * @param id    signDateId
     * @return 计数
     */
    int updateMemberCount(@Param("count") int count, @Param("id") Integer id);

    /**
     * 查询用户积分
     *
     * @param uid 用户id
     * @return UserScore
     */
    UserScore getOneByUid(@Param("uid") Integer uid);


    /**
     * 修改用户补签数
     *
     * @param uid   用户id
     * @param count 补签次数:在原有基础上进行加减
     * @return 计数
     */
    int updateUserRetroactiveCount(@Param("uid") Integer uid, @Param("count") Integer count);

    /**
     * 修改用户积分
     *
     * @param score   积分
     * @param userIds 用户id
     * @return 计数
     */
    int updateMultipleUserScore(@Param("score") Integer score, @Param("userIds") Set<Integer> userIds);

    /**
     * 查询用户积分
     *
     * @param userIds 用户ids
     * @return List
     */
    List<UserScore> listUserScore(@Param("userIds") Set<Integer> userIds);
}
