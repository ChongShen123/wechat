package com.xsdkj.wechat.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.xsdkj.wechat.bo.MsgBox;
import com.xsdkj.wechat.bo.UserDetailsBo;
import com.xsdkj.wechat.common.Cmd;
import com.xsdkj.wechat.common.JsonPage;
import com.xsdkj.wechat.common.JsonPageWithPrice;
import com.xsdkj.wechat.common.JsonResult;
import com.xsdkj.wechat.constant.*;
import com.xsdkj.wechat.dto.UserPriceOperationDto;
import com.xsdkj.wechat.dto.UserPriceOperationLogDto;
import com.xsdkj.wechat.entity.chat.SingleChat;
import com.xsdkj.wechat.entity.user.User;
import com.xsdkj.wechat.entity.user.UserOperationLog;
import com.xsdkj.wechat.entity.wallet.WalletPriceChangeLog;
import com.xsdkj.wechat.entity.wallet.Wallet;
import com.xsdkj.wechat.entity.wallet.WalletOperationLog;
import com.xsdkj.wechat.entity.wallet.WalletTransferLog;
import com.xsdkj.wechat.mapper.*;
import com.xsdkj.wechat.service.BaseService;
import com.xsdkj.wechat.service.SingleChatService;
import com.xsdkj.wechat.service.UserService;
import com.xsdkj.wechat.service.UserWalletService;
import com.xsdkj.wechat.ex.*;
import com.xsdkj.wechat.thread.GetUserByIdServiceThread;
import com.xsdkj.wechat.util.*;
import com.xsdkj.wechat.vo.admin.UserOperationLogVo;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static com.xsdkj.wechat.netty.cmd.base.BaseHandler.sendMessage;


/**
 * @author tiankong
 * @date 2020/1/3 10:37
 */
@Service
@Slf4j
@Transactional
public class UserWalletServiceImpl extends BaseService implements UserWalletService {
    @Resource
    private SingleChatService singleChatService;
    @Resource
    private UserService userService;
    @Resource
    private UserUtil userUtil;
    @Resource
    private WalletMapper walletMapper;
    @Resource
    private PasswordEncoder encoder;
    /**
     * 充值提现记录
     */
    @Resource
    private WalletOperationLogMapper walletOperationLogMapper;
    /**
     * 管理员操作记录
     */
    @Resource
    private UserOperationLogMapper userOperationLogMapper;
    /**
     * 用户账变记录
     */
    @Resource
    private WalletPriceChangeLogMapper walletPriceChangeLogMapper;
    /**
     * 用户转账记录
     */
    @Resource
    private WalletTransferLogMapper walletTransferLogMapper;
    @Resource
    private ChatUtil chatUtil;


    @Override
    public Wallet getRedisData(Integer userId) {
        Object data = redisUtil.get(RedisConstant.REDIS_USER_WALLET + userId);
        Wallet wallet;
        if (data != null) {
            wallet = JSONObject.toJavaObject(JSONObject.parseObject(data.toString()), Wallet.class);
        } else {
            wallet = walletMapper.getOneByUid(userId);
            if (wallet == null) {
                return null;
            }
            redisUtil.set(RedisConstant.REDIS_USER_WALLET + userId, JSONObject.toJSONString(wallet), RedisConstant.REDIS_USER_TIMEOUT);
        }
        return wallet;
    }

    @Deprecated
    @Override
    public Wallet getByUid(Integer uid, boolean type) {
        long begin = System.currentTimeMillis();
        try {
            if (type) {
                UserDetailsBo userDetailsBo = userService.getRedisDataByUid(uid);
                Wallet wallet;
                if (userDetailsBo != null) {
                    wallet = userDetailsBo.getWallet();
                    if (wallet == null) {
                        wallet = walletMapper.getOneByUid(uid);
                        if (wallet == null) {
                            log.error("业务异常>>>用户{}钱包不存在", uid);
                            throw new DataEmptyException();
                        }
                        userService.updateRedisDataByUid(uid, wallet);
                    }
                    return wallet;
                }
                wallet = walletMapper.getOneByUid(uid);
                return wallet;
            }
            return walletMapper.getOneByUid(uid);
        } finally {
            log.debug("获取用户钱包完成 {}ms", DateUtil.spendMs(begin));
        }
    }

    @Override
    public List<UserOperationLogVo> listUserPriceOperationLog(UserPriceOperationLogDto userPriceOperationLogDto) {
        if (ObjectUtil.isEmpty(userPriceOperationLogDto.getUid())) {
            return new ArrayList<>();
        }
        PageHelper.startPage(userPriceOperationLogDto.getPageNum(), userPriceOperationLogDto.getPageSize());
        Integer type = userPriceOperationLogDto.getDateType();
        if (type != null) {
            Long[] beginAndEndTimes = TimeUtil.getBeginAndEndTimes(type, userPriceOperationLogDto.getBeginTimes(), userPriceOperationLogDto.getEndTimes());
            userPriceOperationLogDto.setBeginTimes(beginAndEndTimes[0]);
            userPriceOperationLogDto.setEndTimes(beginAndEndTimes[1]);
        }
        Integer uid = userPriceOperationLogDto.getUid();
        int tableNum = uid % SystemConstant.LOG_TABLE_COUNT;
        return walletOperationLogMapper.listUserPriceOperationLog(userPriceOperationLogDto, tableNum);
    }

    @Override
    public void priceOperation(UserPriceOperationDto param) {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        long begin = System.currentTimeMillis();
        User user;
        User admin = userUtil.getUser();
        try {
            user = ThreadUtil.getSingleton().submit(new GetUserByIdServiceThread(userService, countDownLatch, param.getUid())).get();
        } catch (InterruptedException | ExecutionException | NullPointerException e) {
            e.printStackTrace();
            throw new DataEmptyException();
        }
        try {
            countDownLatch.await(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (user == null) {
            throw new UserNotFountException();
        }
        if (!admin.getPlatformId().equals(user.getPlatformId())) {
            System.out.println(admin.getPlatformId());
            System.out.println(user.getPlatformId());
            throw new PermissionDeniedException();
        }
        Wallet userWallet = getRedisData(user.getId());
        if (userWallet == null) {
            userWallet = createNewWallet(user.getId());
            walletMapper.insert(userWallet);
        }
        Integer type = param.getType();
        byte operationType;
        StringBuilder sb = new StringBuilder();
        Wallet finalUserWallet = userWallet;
        switch (type) {
            case SystemConstant.TOP_UP:
                ThreadUtil.getSingleton().execute(() -> handleUserPriceOperation(param, finalUserWallet, user, admin, true));
                operationType = ChatConstant.TOP_UP;
                sb.append(String.format("系统已为您充值%s元,请注意查看钱包余额!", param.getPrice()));
                break;
            case SystemConstant.DRAW_MONEY:
                if (walletMapper.getOneByUid(param.getUid()).getPrice().subtract(param.getPrice()).doubleValue() < 0) {
                    throw new UserWalletBalanceException();
                }
                sb.append(String.format("您已提现%s元,请注意查收!", param.getPrice()));
                operationType = ChatConstant.WITHDRAW;
                ThreadUtil.getSingleton().execute(() -> handleUserPriceOperation(param, finalUserWallet, user, admin, false));
                break;
            default:
                throw new ValidateException();
        }
        ThreadUtil.getSingleton().execute(() -> {
            SingleChat singleChat = chatUtil.createNewSingleChat(param.getUid(), SystemConstant.SYSTEM_USER_ID, sb.toString(), operationType);
            handleUserPriceOperation(singleChat, begin);
        });
    }

    @Override
    public Wallet createNewWallet(Integer uid) {
        Wallet wallet = new Wallet();
        BigDecimal price = new BigDecimal(0);
        long createTimes = System.currentTimeMillis();
        wallet.setPrice(price);
        wallet.setCreateTimes(createTimes);
        wallet.setModifiedTimes(createTimes);
        wallet.setTotalPrice(price);
        wallet.setUid(uid);
        return wallet;
    }

    @Override
    public void save(Wallet wallet) {
        walletMapper.insert(wallet);
    }

    @Override
    public void updateRedisData(Integer uid) {
        Wallet wallet = walletMapper.getOneByUid(uid);
        if (wallet == null) {
            log.debug("用户钱包未找到");
            throw new DataEmptyException();
        }
        redisUtil.set(RedisConstant.REDIS_USER_WALLET + uid, JSONObject.toJSONString(wallet), RedisConstant.REDIS_USER_TIMEOUT);
        log.debug("更新用户钱包完成");
    }

    @Override
    public void updatePayPassword(Integer uid, String password) {
        walletMapper.updatePayPassword(uid, encoder.encode(password));
    }

    @Override
    public void resetPayPassword(Integer uid, String password, String oldPassword) {
        Wallet wallet = walletMapper.getOneByUid(uid);
        if (encoder.matches(oldPassword, wallet.getPassword())) {
            updatePayPassword(uid, password);
            return;
        }
        log.error("旧密码不匹配");
        throw new PasswordNotMatchException();
    }

    /**
     * 处理用户充值提现通知
     *
     * @param singleChat singleChat
     * @param begin      time
     */
    private void handleUserPriceOperation(SingleChat singleChat, long begin) {
        log.debug("开始处理金额操作通知...");
        Integer toUserId = singleChat.getToUserId();
        log.debug("被通知用户id:{} {}ms", toUserId, DateUtil.spendMs(begin));
        Channel userChannel = SessionUtil.ONLINE_USER_MAP.get(toUserId);
        if (userChannel != null) {
            sendMessage(userChannel, JsonResult.success(singleChat, Cmd.SINGLE_CHAT));
            log.debug("已将通知信息发送给该用户 {}ms", DateUtil.spendMs(begin));
            singleChat.setRead(true);
            singleChatService.save(singleChat);
            return;
        }
        singleChat.setRead(false);
        singleChatService.save(singleChat);
        log.debug("该用户不在线,已将通知消息离线保存. {}ms", DateUtil.spendMs(begin));
        log.debug("金额通知完成. 用时{}ms", DateUtil.spendMs(begin));
    }

    /**
     * 处理充值业务
     *
     * @param param  参数
     * @param wallet 用户钱包
     * @param user   用户
     * @param admin  管理员
     * @param type   true为充值 false为提现
     */
    private void handleUserPriceOperation(UserPriceOperationDto param, Wallet wallet, User user, User admin, Boolean type) {
        BigDecimal price = param.getPrice();
        log.debug("本次充值金额:{}", price);
        BigDecimal beforePrice = wallet.getPrice();
        log.debug("充值前用户金额:{}", beforePrice);
        BigDecimal afterPrice;
        // 操作类型 1 充值 2 提现
        int operationType;
        boolean addType;
        if (type) {
            afterPrice = beforePrice.add(price);
            BigDecimal totalPrice = wallet.getTotalPrice().add(price);
            // 总充值
            wallet.setTotalPrice(totalPrice);
            operationType = SystemConstant.TOP_UP;
            addType = true;
        } else {
            afterPrice = beforePrice.subtract(price);
            operationType = SystemConstant.DRAW_MONEY;
            addType = false;
        }
        log.debug("充值后用户金额:{}", afterPrice);
        log.debug("操作类型(1 充值 2 提现):{}", operationType);
        wallet.setPrice(afterPrice);
        int i = walletMapper.updateUserWallet(wallet);
        log.debug("本地钱包是否更新:{}", i);
        int tableNum = user.getId() % SystemConstant.LOG_TABLE_COUNT;
        log.debug("用户路由表:{}", tableNum);
        WalletOperationLog walletPriceLog = createNewWalletOperationLog(user.getId(), admin.getId(), price, beforePrice, afterPrice, operationType, param.getExplain());
        int insert = walletOperationLogMapper.insert(walletPriceLog, tableNum);
        log.debug("充值提现流水表:{} {}", insert, walletPriceLog);
        UserOperationLog userOperationLog = LogUtil.createNewUserOperationLog(admin.getId(), admin.getPlatformId(), operationType, String.format("管理员%s为用户%s充值%s元", admin.getId(), user.getId(), price));
        int insert1 = userOperationLogMapper.insert(userOperationLog);
        log.debug("管理员操作日志: {} {}", insert1, userOperationLog);
        WalletPriceChangeLog walletPriceChangeLog = createNewWalletPriceChangeLog(param.getUid(), param.getPrice(), beforePrice, afterPrice, addType, operationType);
        int insert2 = walletPriceChangeLogMapper.insert(walletPriceChangeLog, tableNum);
        log.debug("用户账变记录日志:{} {}", insert2, walletPriceChangeLog);
        updateRedisData(user.getId());
    }

    /**
     * 创建一条用户账变记录
     *
     * @param uid           用户id
     * @param price         金额
     * @param beforePrice   操作前金额
     * @param afterPrice    操作后金额
     * @param addType       添加类型 true 添加 false 减少
     * @param operationType 类型类型 1充值 2 提现 3 转账 4 红包
     * @return WalletPriceChangeLog
     */
    private WalletPriceChangeLog createNewWalletPriceChangeLog(Integer uid, BigDecimal price, BigDecimal beforePrice, BigDecimal afterPrice, boolean addType, int operationType) {
        WalletPriceChangeLog walletPriceChangeLog = new WalletPriceChangeLog();
        walletPriceChangeLog.setUid(uid);
        walletPriceChangeLog.setPrice(price);
        walletPriceChangeLog.setBeforePrice(beforePrice);
        walletPriceChangeLog.setAfterPrice(afterPrice);
        walletPriceChangeLog.setAddType(addType);
        walletPriceChangeLog.setType((byte) operationType);
        walletPriceChangeLog.setMonth((byte) (DateUtil.thisMonth() + 1));
        walletPriceChangeLog.setYear(DateUtil.thisYear());
        walletPriceChangeLog.setCreateTimes(System.currentTimeMillis());
        return walletPriceChangeLog;
    }


    /**
     * 创建一个金额操作流水记录
     *
     * @param uid         用户id
     * @param adminId     管理员id
     * @param price       操作金额
     * @param beforePrice 操作前用户金额
     * @param afterPrice  操作后用户金额
     * @param type        操作类型 1 充值 2 提现
     * @param explain     说明
     * @return WalletPriceLog
     */
    private WalletOperationLog createNewWalletOperationLog(Integer uid, Integer adminId, BigDecimal price, BigDecimal beforePrice, BigDecimal afterPrice, int type, String explain) {
        WalletOperationLog walletPriceLog = new WalletOperationLog();
        walletPriceLog.setUid(uid);
        walletPriceLog.setOperationId(adminId);
        walletPriceLog.setPrice(price);
        walletPriceLog.setBeforePrice(beforePrice);
        walletPriceLog.setAfterPrice(afterPrice);
        walletPriceLog.setType((byte) type);
        walletPriceLog.setMonth((byte) (DateUtil.thisMonth() + 1));
        walletPriceLog.setYear(DateUtil.thisYear());
        walletPriceLog.setExplain(explain);
        walletPriceLog.setCreateTimes(System.currentTimeMillis());
        return walletPriceLog;
    }

    @Override
    public boolean transferAccounts(Wallet userWallet, Wallet toUserWallet, BigDecimal price) {
        long begin = System.currentTimeMillis();
        log.debug("开始处理转账数据保存工作...");
        Integer uid = userWallet.getUid();
        Integer toUserId = toUserWallet.getUid();
        BigDecimal userBeforePrice = userWallet.getPrice();
        BigDecimal userAfterPrice = userBeforePrice.subtract(price);
        log.debug("用户id:{} 转账金额:{} 转账前金额:{} 转账后金额:{}", uid, price, userBeforePrice, userAfterPrice);
        BigDecimal toUserBeforePrice = toUserWallet.getPrice();
        BigDecimal toUserAfterPrice = toUserBeforePrice.add(price);
        log.debug("接收方用户id:{} 转账金额:{} 转账前金额:{} 转账后金额:{}", toUserId, price, toUserBeforePrice, toUserAfterPrice);
        int userCount = walletMapper.updateWalletPrice(uid, userAfterPrice);
        log.debug("修改用户钱包完成{} {}ms", userCount, DateUtil.spendMs(begin));
        int toUserCount = walletMapper.updateWalletPrice(toUserId, toUserAfterPrice);
        log.debug("修改接收方用户钱包完成{} {}ms", toUserCount, DateUtil.spendMs(begin));
        log.debug("创建账变记录");
        WalletPriceChangeLog userPriceChangeLog = createNewWalletPriceChangeLog(uid, price, userBeforePrice, userAfterPrice, false, WalletConstant.TRANSFER);
        WalletPriceChangeLog toUserPriceChangeLog = createNewWalletPriceChangeLog(toUserId, price, toUserBeforePrice, toUserAfterPrice, true, WalletConstant.TRANSFER);
        int userWalletPriceCount = walletPriceChangeLogMapper.insert(userPriceChangeLog, uid % SystemConstant.LOG_TABLE_COUNT);
        log.debug("用户账变记录完成{} {}ms", userPriceChangeLog, DateUtil.spendMs(begin));
        int toUserWalletPriceCount = walletPriceChangeLogMapper.insert(toUserPriceChangeLog, toUserId % SystemConstant.LOG_TABLE_COUNT);
        log.debug("接收方用户账变记录完成{} {}ms", toUserPriceChangeLog, DateUtil.spendMs(begin));
        log.debug("创建转账记录");
        WalletTransferLog userTransFerLog = createNewWalletTransferLog(uid, toUserId, price, userBeforePrice, userAfterPrice, WalletConstant.SHIFT_OUT);
        WalletTransferLog toUserTransFerLog = createNewWalletTransferLog(toUserId, uid, price, toUserBeforePrice, toUserAfterPrice, WalletConstant.SHIFT_IN);
        int userWalletTransferCount = walletTransferLogMapper.insert(userTransFerLog, uid % SystemConstant.LOG_TABLE_COUNT);
        log.debug("用户转账记录保存完成{} {}ms", userTransFerLog, DateUtil.spendMs(begin));
        int toUserWalletTransferCount = walletTransferLogMapper.insert(toUserTransFerLog, toUserId % SystemConstant.LOG_TABLE_COUNT);
        log.debug("接收方用户转账记录保存完成{} {}ms", toUserTransFerLog, DateUtil.spendMs(begin));
        boolean flag = userCount + toUserCount + userWalletPriceCount + toUserWalletPriceCount + userWalletTransferCount + toUserWalletTransferCount == 6;
        log.debug("数据保存工作处理完成 {}ms", DateUtil.spendMs(begin));
        return flag;
    }

    /**
     * 创建转账记录
     *
     * @param uid             用户id
     * @param effectId        受影响用户id
     * @param price           金额
     * @param userBeforePrice 操作前用户金额
     * @param userAfterPrice  操作后用户金额
     * @param shiftType       操作类型: 0转出 1转入
     * @return WalletTransferLog
     */
    private WalletTransferLog createNewWalletTransferLog(Integer uid, Integer effectId, BigDecimal price, BigDecimal userBeforePrice, BigDecimal userAfterPrice, byte shiftType) {
        WalletTransferLog transferLog = new WalletTransferLog();
        transferLog.setUid(uid);
        switch (shiftType) {
            case WalletConstant.SHIFT_IN:
                transferLog.setFromUserId(effectId);
                break;
            case WalletConstant.SHIFT_OUT:
                transferLog.setToUserId(effectId);
                break;
            default:
                log.error("业务异常>>>创建转账记录出错!");
        }
        transferLog.setPrice(price);
        transferLog.setBeforePrice(userBeforePrice);
        transferLog.setAfterPrice(userAfterPrice);
        transferLog.setType(shiftType);
        transferLog.setMonth((byte) (DateUtil.thisMonth() + 1));
        transferLog.setYear(DateUtil.thisYear());
        transferLog.setCreateTimes(System.currentTimeMillis());
        return transferLog;
    }
}
