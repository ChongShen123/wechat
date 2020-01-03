package com.xsdkj.wechat.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.text.StrBuilder;
import cn.hutool.core.util.StrUtil;
import com.xsdkj.wechat.bo.MsgBox;
import com.xsdkj.wechat.constant.ChatConstant;
import com.xsdkj.wechat.constant.RabbitConstant;
import com.xsdkj.wechat.constant.SystemConstant;
import com.xsdkj.wechat.dto.UserPriceOperationDto;
import com.xsdkj.wechat.entity.chat.SingleChat;
import com.xsdkj.wechat.entity.user.User;
import com.xsdkj.wechat.entity.user.UserOperationLog;
import com.xsdkj.wechat.entity.wallet.Wallet;
import com.xsdkj.wechat.entity.wallet.WalletPriceLog;
import com.xsdkj.wechat.mapper.WalletMapper;
import com.xsdkj.wechat.service.BaseService;
import com.xsdkj.wechat.service.UserService;
import com.xsdkj.wechat.service.UserWalletService;
import com.xsdkj.wechat.service.ex.PermissionDeniedException;
import com.xsdkj.wechat.service.ex.UserNotFountException;
import com.xsdkj.wechat.service.ex.UserWalletBalanceException;
import com.xsdkj.wechat.service.ex.ValidateException;
import com.xsdkj.wechat.util.ChatUtil;
import com.xsdkj.wechat.util.UserUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
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
    private ChatUtil chatUtil;

    @Override
    public Wallet getByUid(Integer uid) {
        return walletMapper.getOneByUid(uid);
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

    private Wallet createNewWallet(Integer uid) {
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
        if (type) {
            afterPrice = beforePrice.add(price);
            BigDecimal totalPrice = wallet.getTotalPrice().add(price);
            // 总充值
            wallet.setTotalPrice(totalPrice);
            operationType = SystemConstant.TOP_UP;
        } else {
            afterPrice = beforePrice.subtract(price);
            operationType = SystemConstant.DRAW_MONEY;
        }
        wallet.setPrice(afterPrice);
        // 用户钱包添加
        rabbitTemplateService.addExchange(RabbitConstant.FANOUT_SERVICE_NAME, MsgBox.create(RabbitConstant.BOX_TYPE_UPDATE_USER_WALLET, wallet));
        // 创建一个金额操作流水记录
        WalletPriceLog walletPriceLog = createNewWalletPriceLog(user.getId(), admin.getId(), price, beforePrice, afterPrice, operationType);
        rabbitTemplateService.addExchange(RabbitConstant.FANOUT_SERVICE_NAME, MsgBox.create(RabbitConstant.BOX_TYPE_USER_PRICE_OPERATION_LOG, walletPriceLog));
        // 创建管理员操作记录
        UserOperationLog userOperationLog = createNewUserOperationLog(admin.getId(), admin.getPlatformId(), operationType, String.format("管理员%s为用户%s充值%s元", admin.getId(), user.getId(), price));
        rabbitTemplateService.addExchange(RabbitConstant.FANOUT_SERVICE_NAME, MsgBox.create(RabbitConstant.BOX_TYPE_USER_OPERATION_LOG, userOperationLog));
        return wallet;
    }


    /**
     * 创建管理员操作记录
     *
     * @param uid        用户id
     * @param platformId 平台id
     * @param type       操作类型 1 上分,2 下分,3 禁言,4 设置群可加好友 5 加签到次数,6 减签到次数 ...
     * @param content    操作内容
     * @return UserOperationLog
     */
    private UserOperationLog createNewUserOperationLog(Integer uid, Integer platformId, int type, String content) {
        UserOperationLog log = new UserOperationLog();
        log.setUid(uid);
        log.setPlatfromId(platformId);
        log.setOperationType((byte) type);
        log.setContent(content);
        log.setCreateTimes(System.currentTimeMillis());
        return log;
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
     * @return WalletPriceLog
     */
    private WalletPriceLog createNewWalletPriceLog(Integer uid, Integer adminId, BigDecimal price, BigDecimal beforePrice, BigDecimal afterPrice, int type) {
        WalletPriceLog walletPriceLog = new WalletPriceLog();
        walletPriceLog.setUid(uid);
        walletPriceLog.setOperationId(adminId);
        walletPriceLog.setPrice(price);
        walletPriceLog.setBeforePrice(beforePrice);
        walletPriceLog.setAfterPrice(afterPrice);
        walletPriceLog.setType((byte) type);
        walletPriceLog.setMonth((byte) (DateUtil.thisMonth() + 1));
        walletPriceLog.setYear(DateUtil.thisYear());
        walletPriceLog.setCreateTimes(System.currentTimeMillis());
        return walletPriceLog;
    }

}
