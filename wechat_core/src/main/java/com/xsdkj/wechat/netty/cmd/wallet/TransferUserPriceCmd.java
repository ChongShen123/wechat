package com.xsdkj.wechat.netty.cmd.wallet;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.xsdkj.wechat.common.Cmd;
import com.xsdkj.wechat.common.JsonResult;
import com.xsdkj.wechat.constant.ChatConstant;
import com.xsdkj.wechat.constant.ParamConstant;
import com.xsdkj.wechat.entity.chat.SingleChat;
import com.xsdkj.wechat.entity.user.User;
import com.xsdkj.wechat.entity.wallet.Wallet;
import com.xsdkj.wechat.ex.*;
import com.xsdkj.wechat.netty.cmd.CmdAnno;
import com.xsdkj.wechat.netty.cmd.base.AbstractChatCmd;
import com.xsdkj.wechat.service.UserWalletService;
import com.xsdkj.wechat.util.SessionUtil;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 * 用户转账
 *
 * @author tiankong
 * @date 2020/1/3 15:27
 */
@Slf4j
@CmdAnno(cmd = Cmd.TRANSFER_ACCOUNTS)
@Component
public class TransferUserPriceCmd extends AbstractChatCmd {
    @Resource
    private UserWalletService userWalletService;
    @Resource
    private PasswordEncoder encoder;

    @Override
    protected void parseParam(JSONObject param) {
        int toUserId = Integer.parseInt(parseParam(param, ParamConstant.KEY_TO_USER_ID));
        requestParam.setToUserId(toUserId);
        if (toUserId == (session.getUid())) {
            log.error("不能转账给自己");
            throw new IllegalOperationException();
        }
        requestParam.setPrice(new BigDecimal(parseParam(param, ParamConstant.KEY_PRICE)));
        requestParam.setPassword(parseParam(param, ParamConstant.KEY_PASSWORD));
        String content = param.getString(ParamConstant.KEY_CONTENT);
        if (StrUtil.isNotBlank(content)) {
            requestParam.setContent(content);
        }
    }

    @Override
    protected void concreteAction(Channel channel) throws RuntimeException {
        long begin = System.currentTimeMillis();
        log.debug("开始处理转账业务...");
        Integer toUserId = requestParam.getToUserId();
        BigDecimal price = requestParam.getPrice();
        String content = requestParam.getContent();
        String password = requestParam.getPassword();
        Wallet wallet = userWalletService.getByUid(session.getUid(), false);
        if (StrUtil.isBlank(wallet.getPassword())) {
            log.error("支付密码为空");
            throw new PayPasswordIsEmptyException();
        }
        if (!encoder.matches(password, wallet.getPassword())) {
            log.error("支付密码不正确");
            throw new PasswordNotMatchException();
        }
        log.debug("判断用户余额是否足够");
        Wallet userWallet = userWalletService.getByUid(session.getUid(), true);
        if (userWallet.getPrice().subtract(price).doubleValue() < 0) {
            log.error("用户余额不足");
            throw new UserBalancePriceException();
        }
        log.debug("判断对方是否存在");
        User toUser = userService.getRedisUserByUserId(toUserId);
        if (toUser == null) {
            log.error("用户数据为空");
            throw new DataEmptyException();
        }
        Wallet toUserWallet = userWalletService.getByUid(toUserId, true);
        boolean flag = userWalletService.transferAccounts(userWallet, toUserWallet, price);
        if (!flag) {
            log.error("系统异常>>>保存用户转账数据出错");
            throw new SystemException();
        }
        SingleChat newSingleChat = chatUtil.createNewSingleChat(
                toUserId, session.getUid(),
                StrUtil.isBlank(content) ? String.format("来自%s的转账信息,请注册查收!", session.getUsername()) : content,
                ChatConstant.CHAT_TYPE_TRANSFER);
        log.debug("通知对方转账消息{} {}ms", newSingleChat, DateUtil.spendMs(begin));
        Channel toUserChannel = SessionUtil.getUserChannel(toUserId);
        if (toUserChannel != null) {
            newSingleChat.setRead(true);
            sendMessage(toUserChannel, JsonResult.success(newSingleChat, cmd));
        } else {
            newSingleChat.setRead(false);
        }
        sendMessage(channel, JsonResult.success());
        log.debug("已通知用户{} {}ms", session.getUid(), DateUtil.spendMs(begin));
        userService.updateRedisDataByUid(session.getUid(), "TransferUserPriceCmd.concreteAction() 用户转账更新缓存");
        userService.updateRedisDataByUid(toUserId, "TransferUserPriceCmd.concreteAction() 转账更新对方缓存");
        singleChatService.save(newSingleChat);
        log.debug("转账业务处理完成 {}ms", DateUtil.spendMs(begin));

    }
}
