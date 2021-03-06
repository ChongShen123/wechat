package com.cxkj.wechat.netty.notice;

import cn.hutool.core.thread.ThreadUtil;
import com.cxkj.wechat.netty.executor.base.BaseHandler;
import com.cxkj.wechat.util.JsonResult;
import com.cxkj.wechat.util.SessionUtil;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

/**
 * 系统推送器
 *
 * @author tiankong
 * @date 2019/11/18 14:12
 */
@Component
@Slf4j
public class SystemNotice extends BaseHandler implements Runnable {
    @Override
    public void run() {
        log.info("已启动通知线程!");
        for (; ; ) {
            if (SessionUtil.ONLINE_USER_MAP.size() > 0) {
                for (Map.Entry<Integer, Channel> integerChannelEntry : SessionUtil.ONLINE_USER_MAP.entrySet()) {
                    sendMessage(integerChannelEntry.getValue(), JsonResult.success("当前时间:" + new Date()));
                }
            }
            ThreadUtil.sleep(1000 * 60 * 10);
        }
    }
}
