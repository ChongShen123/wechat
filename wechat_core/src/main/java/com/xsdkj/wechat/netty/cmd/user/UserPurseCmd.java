package com.xsdkj.wechat.netty.cmd.user;

import com.alibaba.fastjson.JSONObject;
import com.xsdkj.wechat.common.Cmd;
import com.xsdkj.wechat.common.JsonResult;
import com.xsdkj.wechat.entity.wallet.Wallet;
import com.xsdkj.wechat.netty.cmd.CmdAnno;
import com.xsdkj.wechat.netty.cmd.base.AbstractChatCmd;
import io.netty.channel.Channel;
import org.springframework.stereotype.Component;

/**
 * @author tiankong
 * @date 2020/1/3 14:53
 */
@Component
@CmdAnno(cmd = Cmd.USER_PRICE)
public class UserPurseCmd extends AbstractChatCmd {
    @Override
    protected void parseParam(JSONObject param) throws Exception {
    }

    @Override
    protected void concreteAction(Channel channel) throws RuntimeException {
        Wallet wallet = userService.getRedisDataByUid(session.getUid()).getWallet();
        sendMessage(channel, JsonResult.success(wallet == null ? 0 : wallet.getPrice(), cmd));
    }
}
