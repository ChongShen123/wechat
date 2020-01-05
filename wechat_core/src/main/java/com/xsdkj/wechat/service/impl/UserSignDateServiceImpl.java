package com.xsdkj.wechat.service.impl;

import cn.hutool.core.date.DateUtil;
import com.xsdkj.wechat.constant.SystemConstant;
import com.xsdkj.wechat.entity.user.User;
import com.xsdkj.wechat.entity.wallet.SignAward;
import com.xsdkj.wechat.entity.wallet.SignDate;
import com.xsdkj.wechat.entity.wallet.UserScore;
import com.xsdkj.wechat.ex.AlreadySignedInException;
import com.xsdkj.wechat.mapper.SignAwardMapper;
import com.xsdkj.wechat.mapper.SignDateMapper;
import com.xsdkj.wechat.mapper.UserScoreMapper;
import com.xsdkj.wechat.service.UserSignDateService;
import com.xsdkj.wechat.util.UserUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author tiankong
 * @date 2020/1/5 11:29
 */
@Service
@Transactional
@Slf4j
public class UserSignDateServiceImpl implements UserSignDateService {
    @Resource
    private SignDateMapper signDateMapper;
    @Resource
    private UserScoreMapper userScoreMapper;
    @Resource
    private SignAwardMapper signAwardMapper;
    @Resource
    private UserUtil userUtil;

    @Override
    public void singDate() {
        long begin = System.currentTimeMillis();
        // 判断用户是否存在
        User user = userUtil.currentUser().getUser();
        Integer userId = user.getId();
        Integer platformId = user.getPlatformId();
        Date day = new Date();
        // 查询今天的签到日期表
        SignDate signDate = signDateMapper.getOneByDayAndPlatformId(day, platformId);
        if (signDate == null) {
            // 如果是第一个签到则创建一个日期
            signDate = createNewSignDate(day, platformId);
            signDateMapper.insert(signDate);
        }
        int signDateCount = signDateMapper.countUserSignDateRelation(userId, signDate.getId());
        // 用户未签到
        if (signDateCount == 0) {
            // 插入用户签到关系表
            signDateMapper.saveUserSingDateRelation(user.getId(), signDate.getId());
            // 判断用户是否为连续签到
            UserScore userScore = signDateMapper.getUserScore(userId);
            if (userScore == null) {
                userScore = createNewUserScore(userId);
                userScoreMapper.insert(userScore);
            }
            Integer retroactiveCount = userScore.getRetroactiveCount();
            // 如果补签次数小于3则补签次数添加
            if (retroactiveCount < SystemConstant.RETROACTIVE_COUNT) {
                userScore.setRetroactiveCount(++retroactiveCount);
            }
            // 上次签到时间
            Long lastSignDateTimes = userScore.getLastSignDateTimes();
            if (lastSignDateTimes == null || lastSignDateTimes < DateUtil.beginOfDay(DateUtil.yesterday()).getTime()) {
                // 不是连接签到则设置连接签到天数为0
                userScore.setSuccessionCount(0);
            }
            // 根据连续签到规则修改用户的积分
            Integer successionCount = userScore.getSuccessionCount();
            Integer score = getSignAward(successionCount);
            log.debug("用户{}签到积分:{}", user.getUsername(), score);
            userScoreMapper.updateUserScore(score, userId, ++successionCount, userScore.getRetroactiveCount());
            // 签到表对应平台签到人数+1
            userScoreMapper.updateMemberCount(1, platformId);
            log.debug("用户{}签到用时:{}毫秒", user.getUsername(), (System.currentTimeMillis() - begin));
            return;
        }
        throw new AlreadySignedInException();
    }

    /**
     * 根据签到天数获取积分奖励
     *
     * @param successionCount 连接天数
     * @return 积分奖励
     */
    private Integer getSignAward(Integer successionCount) {
        Integer score = 1;
        List<SignAward> signAwards = signAwardMapper.listAll();
        for (SignAward signAward : signAwards) {
            Integer awardSuccessionCount = signAward.getSuccessionCount();
            if (awardSuccessionCount > successionCount) {
                return signAward.getAwardScore();
            }
        }
        return score;
    }

    private UserScore createNewUserScore(Integer userId) {
        UserScore userScore = new UserScore();
        userScore.setUid(userId);
        userScore.setRetroactiveCount(0);
        userScore.setSuccessionCount(0);
        userScore.setScore(0);
        userScore.setLastSignDateTimes(System.currentTimeMillis());
        return userScore;
    }

    private SignDate createNewSignDate(Date date, Integer platformId) {
        SignDate signDate = new SignDate();
        signDate.setDay(date);
        signDate.setPlatformId(platformId);
        signDate.setCount(0);
        return signDate;
    }
}
