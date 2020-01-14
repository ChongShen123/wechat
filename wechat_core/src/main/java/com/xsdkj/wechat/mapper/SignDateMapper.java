package com.xsdkj.wechat.mapper;

import com.xsdkj.wechat.dto.UserSignDateDetailDto;
import com.xsdkj.wechat.entity.wallet.SignDate;
import com.xsdkj.wechat.entity.wallet.UserScore;
import com.xsdkj.wechat.vo.UserSignDateDetailVo;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * @author tiankong
 * @date 2020/1/6 15:01
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
    int saveUserSingDateRelation(@Param("uid") Integer uid, @Param("signDateId") Integer signDateId, @Param("score") Integer score);

    /**
     * 查询用户积分
     *
     * @param userId 用户id
     * @return UserScore
     */
    UserScore getUserScore(@Param("userId") Integer userId);

    /**
     * 具体Date查询记录
     *
     * @param day day
     * @return SignDate
     */
    SignDate getOneByDay(@Param("day") Date day);

    /**
     * query user sign date detail
     *
     * @param param param
     * @return list
     */
    List<UserSignDateDetailVo> listUserSignDateDetail(UserSignDateDetailDto param);
}
