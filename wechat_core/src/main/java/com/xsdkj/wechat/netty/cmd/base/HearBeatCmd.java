package com.xsdkj.wechat.netty.cmd.base;

import com.alibaba.fastjson.JSONObject;
import com.xsdkj.wechat.common.Cmd;
import com.xsdkj.wechat.common.JsonResult;
import com.xsdkj.wechat.netty.cmd.CmdAnno;
import io.netty.channel.Channel;
import org.springframework.stereotype.Component;

/**
 * 心跳处理器
 *
 * @author tiankong
 * @date 2019/12/25 15:09
 */
@Component
@CmdAnno(cmd = Cmd.HEARTBEAT)
public class HearBeatCmd extends BaseChatCmd {
    @Override
    protected void parseParam(JSONObject param) throws Exception {

    }

    @Override
    protected void concreteAction(Channel channel) {
        sendMessage(channel, JsonResult.success(cmd));
    }
}
