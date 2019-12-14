package com.cxkj.wechat.netty.executor.base;

import com.alibaba.fastjson.JSONObject;
import com.cxkj.wechat.bo.Session;
import com.cxkj.wechat.constant.SystemConstant;
import com.cxkj.wechat.entity.FriendApplication;
import com.cxkj.wechat.service.FriendApplicationService;
import com.cxkj.wechat.service.FriendService;
import com.cxkj.wechat.service.cache.UserCache;
import io.netty.channel.Channel;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author tiankong
 * @date 2019/12/12 15:58
 */
@Service
public class FriendExecutor extends Executor {
    @Resource
    protected FriendApplicationService friendApplicationService;
    @Resource
    protected UserCache userCache;

    @Resource
    protected FriendService friendService;

    @Override
    public void execute(JSONObject param, Channel channel) {

    }

    /**
     * 获取一条好友申请或回复 消息
     *
     * @param session 发送方
     * @param fid     接收方
     * @param message 消息
     * @param type    0 添加好友 1 回复好友
     * @return 一条消息
     */
    protected FriendApplication getFriendApplication(Session session, Integer fid, String message, byte type) {
        FriendApplication application = new FriendApplication();
        application.setFromUserId(session.getUserId());
        application.setFromUsername(session.getUsername());
        application.setFromUserIcon(session.getIcon());
        application.setToUserId(fid);
        application.setMessage(message);
        application.setState(SystemConstant.UNTREATED);
        application.setRead(false);
        application.setType(type);
        long createTimes = System.currentTimeMillis();
        application.setCreateTimes(createTimes);
        application.setModifiedTime(createTimes);
        return application;
    }
}
