package com.xsdkj.wechat.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.xsdkj.wechat.constant.SystemConstant;
import com.xsdkj.wechat.constant.UserConstant;
import com.xsdkj.wechat.dto.GiveRetroactiveCountDto;
import com.xsdkj.wechat.dto.GiveScoreDto;
import com.xsdkj.wechat.entity.user.User;
import com.xsdkj.wechat.entity.user.UserOperationLog;
import com.xsdkj.wechat.entity.wallet.SignAward;
import com.xsdkj.wechat.entity.wallet.SignDate;
import com.xsdkj.wechat.entity.wallet.UserScore;
import com.xsdkj.wechat.ex.*;
import com.xsdkj.wechat.mapper.SignAwardMapper;
import com.xsdkj.wechat.mapper.SignDateMapper;
import com.xsdkj.wechat.mapper.UserOperationLogMapper;
import com.xsdkj.wechat.mapper.UserScoreMapper;
import com.xsdkj.wechat.service.UserService;
import com.xsdkj.wechat.service.UserSignDateService;
import com.xsdkj.wechat.util.LogUtil;
import com.xsdkj.wechat.util.UserUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.PersistenceException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author tiankong
 * @date 2020/1/5 11:29
 */
@Slf4j
@Service
@Transactional
public class UserSignDateServiceImpl implements UserSignDateService {
    @Resource
    private SignDateMapper signDateMapper;
    @Resource
    private UserScoreMapper userScoreMapper;
    @Resource
    private SignAwardMapper signAwardMapper;
    @Resource
    private UserUtil userUtil;
    @Resource
    private UserService userService;
    @Resource
    private UserOperationLogMapper userOperationLogMapper;

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
            if (retroactiveCount < SystemConstant.MAX_RETROACTIVE_COUNT) {
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

    @Override
    public void giveRetroactiveCount(GiveRetroactiveCountDto giveRetroactiveCountDto) {
        Integer uid = giveRetroactiveCountDto.getUid();
        Integer count = giveRetroactiveCountDto.getCount();
        User admin = userUtil.currentUser().getUser();
        User user = userService.getUserById(uid, false);
        if (admin.getType().equals(UserConstant.TYPE_ADMIN)) {
            if (user != null) {
                if (user.getPlatformId().equals(admin.getPlatformId())) {
                    UserScore userScore = userScoreMapper.getOneByUid(uid);
                    if (userScore != null) {
                        userScoreMapper.updateUserRetroactiveCount(uid, count);
                        UserOperationLog newUserOperationLog = LogUtil.createNewUserOperationLog(admin.getId(), admin.getPlatformId(), SystemConstant.LOG_TYPE_RETROACTIVE_COUNT, String.format("管理员%s为用户%s添加补签次数:%s", admin.getId(), user.getId(), count));
                        userOperationLogMapper.insert(newUserOperationLog);
                        return;
                    }
                    throw new DataEmptyException();
                }
                throw new PermissionDeniedException();
            }
            log.error("用户数据不存在:{}", uid);
            throw new UserNotFountException();
        }
        log.warn("用户{} 异常操作", admin.getId());
        throw new PermissionDeniedException();
    }

    @Override
    public void giveScore(GiveScoreDto giveScoreDto) {
        log.debug("-------------------------");
        log.debug("开始批量修改用户积分...");
        long begin = System.currentTimeMillis();
        User user = userUtil.currentUser().getUser();
        if (user.getType().equals(UserConstant.TYPE_ADMIN)) {
            Set<Integer> userIds;
            Integer score;
            try {
                userIds = Arrays.stream(StrUtil.splitToInt(giveScoreDto.getUserIds(), ",")).boxed().collect(Collectors.toSet());
                score = giveScoreDto.getScore();
            } catch (Exception e) {
                log.error("参数解析出错");
                throw new ValidateException();
            }
            int userIdsCount = userService.countUserIds(userIds);
            if (userIdsCount == userIds.size()) {
                checkUserScore(userIds);
                int count = 0;
                try {
                    count = userScoreMapper.updateMultipleUserScore(score, userIds);
                } catch (DataIntegrityViolationException e) {
                    log.error("非法的操作!用户积分操作后出现负数!{}", userIds);
                    throw new IllegalOperationException();
                }
                if (count == userIds.size()) {
                    userOperationLogMapper.insert(LogUtil.createNewUserOperationLog(user.getId(),
                            user.getPlatformId(),
                            SystemConstant.LOG_TYPE_RETROACTIVE_COUNT,
                            String.format("管理员%s为用户:%s添加积分%s", user.getId(), userIds, score)));
                    log.debug("批量修改用户积分完成!用时{}ms", System.currentTimeMillis() - begin);
                    return;
                }
                log.error("传入的用户id与更新的数据条数不一至!");
                throw new UpdateDataException();
            }
            log.error("数据错误!部分用户不存在!请检查传入的uid包含的用户是否都存在:{}", userIds);
            throw new UserNotFountException();
        }
        log.error("{}不是管理员", user.getUsername());
        throw new PersistenceException();
    }

    /**
     * 检查用户积分是否有记录,没有则创建一个
     *
     * @param userIds 用户ids
     */
    private void checkUserScore(Set<Integer> userIds) {
        log.debug("检查用户是否都有积分记录...");
        long begin = System.currentTimeMillis();
        List<UserScore> userScores = userScoreMapper.listUserScore(userIds);
        if (userScores.size() < userIds.size()) {
            List<Integer> existUserIds = new ArrayList<>();
            userScores.forEach(userScore -> existUserIds.add(userScore.getUid()));
            List<Integer> missUserIds = new ArrayList<>(CollUtil.disjunction(userIds, existUserIds));
            missUserIds.forEach(uid -> {
                userScoreMapper.insert(createNewUserScore(uid));
                log.debug("为用户{}创建了一个积分记录", uid);
            });
        }
        log.debug("检查用户是否都有积分记录完成!用时:{}ms", System.currentTimeMillis() - begin);
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
