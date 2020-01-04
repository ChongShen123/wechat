package com.xsdkj.wechat.netty.cmd.wallet;

import com.alibaba.fastjson.JSONObject;
import com.xsdkj.wechat.common.Cmd;
import com.xsdkj.wechat.common.JsonResult;
import com.xsdkj.wechat.entity.wallet.Wallet;
import com.xsdkj.wechat.netty.cmd.CmdAnno;
import com.xsdkj.wechat.netty.cmd.base.AbstractChatCmd;
import com.xsdkj.wechat.service.UserWalletService;
import io.netty.channel.Channel;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author tiankong
 * @date 2020/1/3 14:53
 */
@Component
@CmdAnno(cmd = Cmd.USER_PRICE)
public class FindUserPriceCmd extends AbstractChatCmd {
    @Resource
    private UserWalletService walletService;
    @Override
    protected void parseParam(JSONObject param) {
    }

    @Override
    protected void concreteAction(Channel channel) throws RuntimeException {
        Wallet wallet = walletService.getByUid(session.getUid(), true);
        sendMessage(channel, JsonResult.success(wallet == null ? 0 : wallet.getPrice(), cmd));
    }
}
