package com.xsdkj.wechat.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.xsdkj.wechat.constant.SystemConstant;
import com.xsdkj.wechat.constant.UserConstant;
import com.xsdkj.wechat.dto.GiveRetroactiveCountDto;
import com.xsdkj.wechat.dto.GiveScoreDto;
import com.xsdkj.wechat.dto.RetroactiveDto;
import com.xsdkj.wechat.dto.UserSignDateDto;
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
import com.xsdkj.wechat.service.SystemService;
import com.xsdkj.wechat.service.UserService;
import com.xsdkj.wechat.service.UserSignDateService;
import com.xsdkj.wechat.util.LogUtil;
import com.xsdkj.wechat.util.ThreadUtil;
import com.xsdkj.wechat.util.UserUtil;
import com.xsdkj.wechat.vo.UserSignDateVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.PersistenceException;
import java.util.*;
import java.util.stream.Collectors;

import static com.xsdkj.wechat.util.BeanUtil.createNewUserScore;

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
    private SystemService systemParameterService;
    @Resource
    private UserOperationLogMapper userOperationLogMapper;

    @Override
    public void handleSignDate() {
        log.debug(LogUtil.INTERVAL);

        User user = userUtil.currentUser().getUser();
        log.debug("开始处理用户{}({})签到业务...", user.getUsername(), user.getId());
        long begin = System.currentTimeMillis();
        Integer platformId = user.getPlatformId();
        Date day = new Date();
        // 查询今天的签到日期表
        SignDate signDate = signDateMapper.getOneByDayAndPlatformId(day, platformId);
        if (signDate == null) {
            // 如果是第一个签到则创建一个日期
            signDate = createNewSignDate(day, platformId);
            signDateMapper.insert(signDate);
        }
        handleSignDate(user, signDate, true);
        log.debug("用户{}签到完成,用时:{}", user.getUsername(), (System.currentTimeMillis() - begin));
    }

    @Override
    public void handleRetroactive(RetroactiveDto retroactiveDto) {
        log.debug(LogUtil.INTERVAL);
        User currentUser = userUtil.currentUser().getUser();
        log.debug("开始处理用户{}({})补签业务...", currentUser.getUsername(), currentUser.getId());
        long begin = System.currentTimeMillis();
        Date date = retroactiveDto.getDate();
        if (DateUtil.endOfDay(new Date()).getTime() < DateUtil.endOfDay(date).getTime()) {
            log.error("签到日期非法");
            throw new IllegalOperationException();
        }
        Integer retroactiveTimeFrame = systemParameterService.getSystemParameter(false).getRetroactiveTimeFrame();
        long currentRetroactive = DateUtil.between(new Date(), date, DateUnit.DAY);
        log.debug("最大补签天数区间为:{}天 当前补签天数为:{}", retroactiveTimeFrame, currentRetroactive);
        if (currentRetroactive <= retroactiveTimeFrame) {
            log.debug("检查用户剩余补签几次");
            UserScore userScore = userScoreMapper.getOneByUid(currentUser.getId());
            if (userScore == null) {
                log.info("用户积分数据为空,准备为用户初始化积分");
                userScore = createNewUserScore(currentUser.getId());
                userScoreMapper.insert(userScore);
            }
            Integer retroactiveCount = userScore.getRetroactiveCount();
            log.debug("用户剩余补签次数:{}", retroactiveCount);
            if (retroactiveCount > 0) {
                SignDate signDate = signDateMapper.getOneByDay(date);
                if (signDate != null) {
                    handleRetroactiveCount(currentUser, signDate);
                    log.debug("补签业务完成用时:{}ms", DateUtil.spendMs(begin));
                    return;
                }
                log.warn("要补签的日期{}不存在,准备创建该日期", DateUtil.formatDate(date));
                SignDate newSignDate = createNewSignDate(date, currentUser.getPlatformId());
                signDateMapper.insert(newSignDate);
                handleRetroactiveCount(currentUser, newSignDate);
                log.debug("补签业务完成用时:{}ms", DateUtil.spendMs(begin));
                return;
            }
            log.error("用户补签次数已用完!");
            throw new RetroactiveCountUseUpException();
        }
        log.error("补签失败!超出补签限制天数");
        throw new BeyondRetroactiveException();
    }

    /**
     * 处理一个用户签到
     *
     * @param user     签到用户
     * @param signDate 签到日期
     * @param flag     是否赠送补签次数(正常签到是需要赠送的,补签则不赠送.)
     */
    private void handleSignDate(User user, SignDate signDate, boolean flag) {
        long begin = System.currentTimeMillis();
        Integer userId = user.getId();
        int signDateCount = signDateMapper.countUserSignDateRelation(userId, signDate.getId());
        if (signDateCount == 0) {
            log.debug("插入用户签到关系表");
            signDateMapper.saveUserSingDateRelation(user.getId(), signDate.getId());
            UserScore userScore = signDateMapper.getUserScore(userId);
            if (userScore == null) {
                log.info("用户积分数据为空,准备为用户初始化积分");
                userScore = createNewUserScore(userId);
                userScoreMapper.insert(userScore);
            }
            Integer retroactiveCount = userScore.getRetroactiveCount();
            if (flag) {
                // 如果补签次数小于3则补签次数添加
                int maxRetroactiveCount = systemParameterService.getSystemParameter(false).getMaxRetroactiveCount();
                log.debug("最大补签赠送为:{},当前用户补签次数为:{}", maxRetroactiveCount, retroactiveCount);
                if (retroactiveCount < maxRetroactiveCount) {
                    userScore.setRetroactiveCount(++retroactiveCount);
                    log.debug("用户补签次数+1:{}", retroactiveCount);
                }
                log.debug("判断用户是否为连续签到");
            }
            // 上次签到时间
            Long lastSignDateTimes = userScore.getLastSignDateTimes();
            boolean isNotSuccession = lastSignDateTimes == null || lastSignDateTimes < DateUtil.beginOfDay(DateUtil.yesterday()).getTime();
            if (isNotSuccession) {
                // 不是连接签到则设置连接签到天数为0
                userScore.setSuccessionCount(0);
            }
            log.debug("用户是否为连接签到:{}", !isNotSuccession);
            if (lastSignDateTimes != null) {
                log.debug("用户上次签到时间为:{}", new DateTime(lastSignDateTimes));
            }
            // 根据连续签到规则修改用户的积分
            Integer successionCount = userScore.getSuccessionCount();
            Integer score = getSignAward(successionCount);
            log.debug("用户{}签到积分:{}", user.getUsername(), score);
            userScoreMapper.updateUserScore(score, userId, ++successionCount, userScore.getRetroactiveCount());
            // 签到表对应日期签到人数+1
            userScoreMapper.updateMemberCount(1, signDate.getId());
            log.debug("具体完成签到用时:{}", DateUtil.spendMs(begin));
            return;
        }
        log.error("用户已签到:{}", signDate.getDay());
        throw new AlreadySignedInException();

    }


    @Override
    public void handleGiveRetroactiveCount(GiveRetroactiveCountDto giveRetroactiveCountDto) {
        User admin = userUtil.getUser();
        if (!admin.getType().equals(UserConstant.TYPE_ADMIN)) {
            log.warn("用户{} 异常操作", admin.getId());
            throw new PermissionDeniedException();
        }
        String userIds = giveRetroactiveCountDto.getUserIds();
        log.debug("判断用户ids 是否包含非法字符串");
        boolean userIdsBaseCheck = !NumberUtil.isNumber(userIds) && !(userIds.contains(",") || userIds.contains("，"));
        if (userIdsBaseCheck) {
            throw new ValidateException();
        }
        log.debug("切分用户ids");
        Set<Integer> userIdSet = handleUserIdsSplit(userIds);
        log.debug("判断用户是否存在");
        List<User> users = userService.listUserByIds(userIdSet);
        List<Integer> existUserIds = new ArrayList<>();
        for (User user : users) {
            existUserIds.add(user.getId());
        }
        Collection<Integer> existUserColl = CollUtil.disjunction(userIdSet, existUserIds);
        for (User user : users) {
            if (!user.getPlatformId().equals(admin.getPlatformId())) {
                log.debug("管理员权限不足");
                throw new PermissionDeniedException();
            }
        }
        if (existUserColl.size() > 0) {
            throw new CheckUserException(String.format("操作失败!用户数据未找到!%s", existUserColl.toString()));
        }
        existUserIds.clear();
        ThreadUtil.getSingleton().execute(() -> {
            log.debug("判断用户积分记录是否存在,不存在则创建");
            List<UserScore> userScores = userScoreMapper.listUserScore(userIdSet);
            for (UserScore userScore : userScores) {
                existUserIds.add(userScore.getUid());
            }
            Collection<Integer> disjunction = CollUtil.disjunction(userIdSet, existUserIds);
            if (disjunction.size() > 0) {
                for (Integer integer : disjunction) {
                    userScoreMapper.insert(createNewUserScore(integer));
                }
            }
            userScoreMapper.updateSetUserRetroactiveCount(userIdSet, giveRetroactiveCountDto.getCount());
            UserOperationLog newUserOperationLog = LogUtil.createNewUserOperationLog(admin.getId(), admin.getPlatformId(), SystemConstant.LOG_TYPE_RETROACTIVE_COUNT, String.format("管理员%s为用户%s添加补签次数:%s", admin.getId(), userIdSet, giveRetroactiveCountDto.getCount()));
            userOperationLogMapper.insert(newUserOperationLog);
        });
    }

    @Override
    public void handleGiveRetroactiveCountAll(Integer count) {
        if (count == null) {
            throw new ValidateException();
        }
        User admin = userUtil.getUser();
        if (!admin.getType().equals(UserConstant.TYPE_ADMIN)) {
            log.warn("用户{} 异常操作", admin.getId());
            throw new PermissionDeniedException();
        }
        ThreadUtil.getSingleton().execute(() -> {
            userScoreMapper.updateUserRetroactiveCountAll(count);
            UserOperationLog newUserOperationLog = LogUtil.createNewUserOperationLog(admin.getId(), admin.getPlatformId(), SystemConstant.LOG_TYPE_RETROACTIVE_COUNT, String.format("管理员%s为%s平台用户赠送积分%s", admin.getId(), admin.getPlatformId(), count));
            userOperationLogMapper.insert(newUserOperationLog);
        });
    }

    /**
     * 处理用户id切分
     *
     * @param userIds 用户id
     * @return Set
     */
    private Set<Integer> handleUserIdsSplit(String userIds) {
        boolean commaLowerCase = userIds.contains(SystemConstant.COMMA_LOWER_CASE);
        boolean commaUpperCase = userIds.contains(SystemConstant.COMMA_UPPER_CASE);
        if (commaLowerCase && commaUpperCase) {
            throw new ValidateException();
        }
        Set<Integer> collect = new HashSet<>();
        if (commaLowerCase) {
            collect = Arrays.stream(StrUtil.splitToInt(userIds, SystemConstant.COMMA_LOWER_CASE)).boxed().collect(Collectors.toSet());
        } else if (commaUpperCase) {
            collect = Arrays.stream(StrUtil.splitToInt(userIds, SystemConstant.COMMA_UPPER_CASE)).boxed().collect(Collectors.toSet());
        } else if (NumberUtil.isNumber(userIds)) {
            collect.add(Integer.parseInt(userIds));
        } else {
            throw new ValidateException();
        }
        return collect;
    }

    public static void main(String[] args) {
    }


    @Override
    public void handleGiveScore(GiveScoreDto giveScoreDto) {
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
                int count;
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
                    log.debug("批量修改用户积分完成!用时{}", DateUtil.spendMs(begin));
                    return;
                }
                log.error("传入的用户id与更新的数据条数不一至!");
                throw new UpdateDataException();
            }
            log.error("数据错误!部分用户不存在!请检查传入的uid包含的用户是否都存在:{}", userIds);
            throw new UserNotFountException();
        }
        log.error("{}不是管理员", user.getId());
        throw new PersistenceException();
    }

    @Override
    public List<UserSignDateVo> listSignDate(UserSignDateDto userSignDateDto) {
        Integer year = userSignDateDto.getYear();
        Integer month = userSignDateDto.getMonth();
        if (ObjectUtil.isNotEmpty(year) || ObjectUtil.isNotEmpty(month)) {
            if (!(ObjectUtil.isNotEmpty(year) && ObjectUtil.isNotEmpty(month))) {
                throw new ValidateException();
            }
        }
        userSignDateDto.setPlatformId(userUtil.getUser().getPlatformId());
        List<UserSignDateVo> list = userScoreMapper.listUserSignDate(userSignDateDto);
        System.out.println(list);
        return list;
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
     * 处理用户补签业务
     *
     * @param currentUser 用户
     * @param signDate    日期
     */
    private void handleRetroactiveCount(User currentUser, SignDate signDate) {
        handleSignDate(currentUser, signDate, false);
        int count = userScoreMapper.updateUserRetroactiveCount(currentUser.getId(), -1);
        log.debug("扣除用户补签次数,数据库执行响应:{}", count);
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


    private SignDate createNewSignDate(Date date, Integer platformId) {
        SignDate signDate = new SignDate();
        signDate.setDay(date);
        signDate.setYear(DateUtil.year(date));
        signDate.setMonth(DateUtil.month(date) + 1);
        signDate.setPlatformId(platformId);
        signDate.setCount(0);
        return signDate;
    }
}
