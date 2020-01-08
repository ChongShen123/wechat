package com.xsdkj.wechat.mapper;

import com.xsdkj.wechat.entity.user.User;
import com.xsdkj.wechat.entity.wallet.UserScore;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

import com.xsdkj.wechat.entity.wallet.SignDate;

/**
 * @author tiankong
 * @date 2020/1/5 11:21
 */
public interface SignDateMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SignDate record);

    int insertSelective(SignDate record);

    SignDate selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SignDate record);

    int updateByPrimaryKey(SignDate record);

    SignDate getOneByDayAndPlatformId(@Param("day") Date day, @Param("platformId") Integer platformId);

    /**
     * 检查用户是否已签到
     *
     * @param userId     用户id
     * @param signDateId 日期id
     * @return 计数
     */
    int countUserSignDateRelation(@Param("userId") Integer userId, @Param("signDateId") Integer signDateId);

    /**
     * 保存用户日期关系记录
     *
     * @param uid        用户id
     * @param signDateId 日期id
     * @return 计数
     */
    int saveUserSingDateRelation(@Param("uid") Integer uid, @Param("signDateId") Integer signDateId);

    /**
     * 查询用户积分
     *
     * @param userId 用户id
     * @return UserScore
     */
    UserScore getUserScore(@Param("userId") Integer userId);


}
