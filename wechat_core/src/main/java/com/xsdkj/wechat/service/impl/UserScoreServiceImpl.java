package com.xsdkj.wechat.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.PageHelper;
import com.xsdkj.wechat.constant.SystemConstant;
import com.xsdkj.wechat.constant.UserConstant;
import com.xsdkj.wechat.dto.GiveScoreDto;
import com.xsdkj.wechat.dto.ListUserScoreDto;
import com.xsdkj.wechat.entity.user.User;
import com.xsdkj.wechat.entity.user.UserOperationLog;
import com.xsdkj.wechat.entity.wallet.UserScore;
import com.xsdkj.wechat.ex.IllegalOperationException;
import com.xsdkj.wechat.ex.UpdateDataException;
import com.xsdkj.wechat.ex.UserNotFountException;
import com.xsdkj.wechat.ex.ValidateException;
import com.xsdkj.wechat.mapper.UserOperationLogMapper;
import com.xsdkj.wechat.mapper.UserScoreMapper;
import com.xsdkj.wechat.service.UserScoreService;
import com.xsdkj.wechat.service.UserService;
import com.xsdkj.wechat.util.LogUtil;
import com.xsdkj.wechat.util.ThreadUtil;
import com.xsdkj.wechat.util.UserUtil;
import com.xsdkj.wechat.vo.UserScoreVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.PersistenceException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import static com.xsdkj.wechat.util.BeanUtil.createNewUserScore;

/**
 * @author tiankong
 * @date 2020/1/6 15:36
 */
@Slf4j
@Service
@Transactional
public class UserScoreServiceImpl implements UserScoreService {
    @Resource
    private UserScoreMapper userScoreMapper;
    @Resource
    @Lazy
    private UserService userService;
    @Resource
    private UserOperationLogMapper userOperationLogMapper;
    @Resource
    private UserUtil userUtil;

    @Override
    public void handleGiveScoreAll(Integer score) {
        User admin = userUtil.getUser();
        userUtil.checkAdminAuthority(admin, UserConstant.TYPE_ADMIN);
        try {
            userScoreMapper.updateUserScoreAll(score, admin.getPlatformId());
        } catch (Exception e) {
            throw new IllegalOperationException();
        }
        ThreadUtil.getSingleton().submit(() -> {
            UserOperationLog newUserOperationLog = LogUtil.createNewUserOperationLog(admin.getId(), admin.getPlatformId(), SystemConstant.LOG_TYPE_UPDATE_SCORE, String.format("管理员%s为%s平台所有用户赠送了积分%s", admin.getId(), admin.getPlatformId(), score));
            userOperationLogMapper.insert(newUserOperationLog);
        });

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
    public UserScoreVo getUserScore() {
        User user = userUtil.currentUser().getUser();
        return userScoreMapper.getUserScoreVoByUid(user.getId());
    }

    @Override
    public List<UserScoreVo> listUserScore(ListUserScoreDto listUserScoreDto) {
        PageHelper.startPage(listUserScoreDto.getPageNum(), listUserScoreDto.getPageSize());
        return userScoreMapper.listUserScoreByVo(listUserScoreDto);
    }

    @Override
    public void save(UserScore newUserScore) {
        userScoreMapper.insert(newUserScore);
    }

    /**
     * 检查用户积分是否有记录,没有则创建一个
     *
     * @param userIds 用户ids
     */
    private void checkUserScore(Set<Integer> userIds) {
        log.debug("检查用户是否都有积分记录...");
        long begin = System.currentTimeMillis();
        List<UserScore> userScores = userScoreMapper.listUserScoreBySet(userIds);
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
}
