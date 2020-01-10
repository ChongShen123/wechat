package com.xsdkj.wechat.netty.cmd.wallet;

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
@Component
@CmdAnno(cmd = Cmd.TRANSFER_ACCOUNTS)
@Slf4j
public class TransferUserPriceCmd extends AbstractChatCmd {
    @Resource
    private UserWalletService userWalletService;
    @Resource
    private PasswordEncoder encoder;

    @Override
    protected void parseParam(JSONObject param) {
        requestParam.setToUserId(Integer.parseInt(parseParam(param, ParamConstant.KEY_TO_USER_ID)));
        requestParam.setPrice(new BigDecimal(parseParam(param, ParamConstant.KEY_PRICE)));
        requestParam.setPassword(parseParam(param, ParamConstant.KEY_PASSWORD));
        String content = param.getString(ParamConstant.KEY_CONTENT);
        if (StrUtil.isNotBlank(content)) {
            requestParam.setContent(content);
        }
    }

    @Override
    protected void concreteAction(Channel channel) throws RuntimeException {
        Integer toUserId = requestParam.getToUserId();
        BigDecimal price = requestParam.getPrice();
        String content = requestParam.getContent();
        String password = requestParam.getPassword();
        Wallet wallet = userWalletService.getByUid(session.getUid(), false);
        if (StrUtil.isBlank(wallet.getPassword())) {
            throw new PayPasswordIsEmptyException();
        }
        if (!encoder.matches(password, wallet.getPassword())) {
            throw new PasswordNotMatchException();
        }
        // 判断用户余额是否足够
        Wallet userWallet = userWalletService.getByUid(session.getUid(), true);
        if (userWallet.getPrice().subtract(price).doubleValue() < 0) {
            throw new UserBalancePriceException();
        }
        // 判断对方是否存在
        User toUser = userService.getRedisUserByUserId(toUserId);
        if (toUser == null) {
            throw new DataEmptyException();
        }
        Wallet toUserWallet = userWalletService.getByUid(toUserId, true);
        // 用户转账
        boolean flag = userWalletService.transferAccounts(userWallet, toUserWallet, price);
        if (!flag) {
            log.error("系统异常>>>修改用户转账钱包出错");
            throw new SystemException();
        }
        // 通知对方转账消息
        SingleChat newSingleChat = chatUtil.createNewSingleChat(
                toUserId, session.getUid(),
                StrUtil.isBlank(content) ? String.format("来自%s的转账信息,请注册查收!", session.getUsername()) : content,
                ChatConstant.CHAT_TYPE_TRANSFER);
        Channel toUserChannel = SessionUtil.getUserChannel(toUserId);
        if (toUserChannel != null) {
            newSingleChat.setRead(true);
            sendMessage(toUserChannel, JsonResult.success(newSingleChat, cmd));
        } else {
            newSingleChat.setRead(false);
        }
        sendMessage(channel, JsonResult.success());
        userService.updateRedisDataByUid(session.getUid(), "TransferUserPriceCmd.concreteAction() 用户转账更新缓存");
        userService.updateRedisDataByUid(toUserId, "TransferUserPriceCmd.concreteAction() 转账更新对方缓存");
        singleChatService.save(newSingleChat);
    }
}
