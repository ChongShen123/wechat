package com.xsdkj.wechat.service.impl;

import cn.hutool.core.date.DateUtil;
import com.xsdkj.wechat.bo.MsgBox;
import com.xsdkj.wechat.bo.UserDetailsBo;
import com.xsdkj.wechat.constant.ChatConstant;
import com.xsdkj.wechat.constant.RabbitConstant;
import com.xsdkj.wechat.constant.SystemConstant;
import com.xsdkj.wechat.constant.WalletConstant;
import com.xsdkj.wechat.dto.UserPriceOperationDto;
import com.xsdkj.wechat.entity.chat.SingleChat;
import com.xsdkj.wechat.entity.user.User;
import com.xsdkj.wechat.entity.user.UserOperationLog;
import com.xsdkj.wechat.entity.wallet.WalletPriceChangeLog;
import com.xsdkj.wechat.entity.wallet.Wallet;
import com.xsdkj.wechat.entity.wallet.WalletOperationLog;
import com.xsdkj.wechat.entity.wallet.WalletTransferLog;
import com.xsdkj.wechat.mapper.*;
import com.xsdkj.wechat.service.BaseService;
import com.xsdkj.wechat.service.UserService;
import com.xsdkj.wechat.service.UserWalletService;
import com.xsdkj.wechat.ex.*;
import com.xsdkj.wechat.util.ChatUtil;
import com.xsdkj.wechat.util.LogUtil;
import com.xsdkj.wechat.util.UserUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;


/**
 * @author tiankong
 * @date 2020/1/3 10:37
 */
@Service
@Slf4j
@Transactional
public class UserWalletServiceImpl extends BaseService implements UserWalletService {
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
    public void priceOperation(UserPriceOperationDto param) {
        User user = userService.getUserById(param.getUid(), false);
        if (user == null) {
            throw new UserNotFountException();
        }
        User admin = userUtil.currentUser().getUser();
        if (!admin.getPlatformId().equals(user.getPlatformId())) {
            throw new PermissionDeniedException();
        }
        Wallet userWallet = walletMapper.getOneByUid(param.getUid());
        if (userWallet == null) {
            userWallet = createNewWallet(user.getId());
            walletMapper.insert(userWallet);
        }
        Integer type = param.getType();
        byte operationType;
        StringBuilder sb = new StringBuilder();
        Wallet redisWallet;
        switch (type) {
            case SystemConstant.TOP_UP:
                redisWallet = handleUserPriceOperation(param, userWallet, user, admin, true);
                operationType = ChatConstant.TOP_UP;
                sb.append(String.format("系统已为您充值%s元,请注意查看钱包余额!", param.getPrice()));
                break;
            case SystemConstant.DRAW_MONEY:
                if (walletMapper.getOneByUid(param.getUid()).getPrice().subtract(param.getPrice()).doubleValue() < 0) {
                    throw new UserWalletBalanceException();
                }
                sb.append(String.format("您已提现%s元,请注意查收!", param.getPrice()));
                operationType = ChatConstant.WITHDRAW;
                redisWallet = handleUserPriceOperation(param, userWallet, user, admin, false);
                break;
            default:
                throw new ValidateException();
        }
        userService.updateRedisDataByUid(param.getUid(), redisWallet);
        SingleChat singleChat = chatUtil.createNewSingleChat(param.getUid(), SystemConstant.SYSTEM_USER_ID, sb.toString(), operationType);
        rabbitTemplateService.addExchange(RabbitConstant.FANOUT_USER_NOTICE_NAME, MsgBox.create(RabbitConstant.USER_PRICE_OPERATION_NOTICE, singleChat));
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
     * 处理充值业务
     *
     * @param param  参数
     * @param wallet 用户钱包
     * @param user   用户
     * @param admin  管理员
     * @param type   true为充值 false为提现
     */
    private Wallet handleUserPriceOperation(UserPriceOperationDto param, Wallet wallet, User user, User admin, Boolean type) {
        // 本次充值金额
        BigDecimal price = param.getPrice();

        // 充值前用户金额
        BigDecimal beforePrice = wallet.getPrice();
        // 充值后用户金额
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
        wallet.setPrice(afterPrice);
        // 用户钱包更新
        walletMapper.updateUserWallet(wallet);
        // 金额操作流水记录
        // 分表路由
        int tableNum = user.getId() % SystemConstant.LOG_TABLE_COUNT;
        WalletOperationLog walletPriceLog = createNewWalletOperationLog(user.getId(), admin.getId(), price, beforePrice, afterPrice, operationType, param.getExplain());
        walletOperationLogMapper.insert(walletPriceLog, tableNum);
        // 管理员操作记录
        UserOperationLog userOperationLog = LogUtil.createNewUserOperationLog(admin.getId(), admin.getPlatformId(), operationType, String.format("管理员%s为用户%s充值%s元", admin.getId(), user.getId(), price));
        userOperationLogMapper.insert(userOperationLog);
        // 用户账变记录
        WalletPriceChangeLog walletPriceChangeLog = createNewWalletPriceChangeLog(param.getUid(), param.getPrice(), beforePrice, afterPrice, addType, operationType);
        walletPriceChangeLogMapper.insert(walletPriceChangeLog, tableNum);
        return wallet;
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
