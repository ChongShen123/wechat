package com.cxkj.wechat.netty.handler;

import com.alibaba.fastjson.JSONObject;
import com.cxkj.wechat.constant.Command;
import com.cxkj.wechat.netty.executor.base.Executor;
import com.cxkj.wechat.util.JsonResult;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 * @author tiankong
 * @date 2019/11/18 13:41
 */
@Service
@Slf4j
@Scope("prototype")
public class HeartBeatHandler extends Executor {
    private int count;

    public HeartBeatHandler() {
        command = Command.HEARTBEAT;
    }

    @Override
    public void execute(JSONObject param, Channel channel) {
        log.info("客户端心跳请求次数:{}", count++);
        sendMessage(channel, JsonResult.success("服务端已收到你的心跳请求", command));
    }
}
