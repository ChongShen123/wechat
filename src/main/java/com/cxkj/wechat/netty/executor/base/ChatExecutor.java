package com.cxkj.wechat.netty.executor.base;

import cn.hutool.core.lang.Snowflake;
import com.alibaba.fastjson.JSONObject;
import com.cxkj.wechat.entity.SingleChat;
import com.cxkj.wechat.service.GroupService;
import com.cxkj.wechat.service.RabbitTemplateService;
import com.cxkj.wechat.service.UserService;
import com.cxkj.wechat.service.cache.GroupCache;
import com.cxkj.wechat.service.cache.UserCache;
import com.cxkj.wechat.util.JwtTokenUtil;
import io.netty.channel.Channel;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 命令执行者抽象类
 *
 * @author tiankong
 * @date 2019/11/17 19:54
 */
@Service
public class ChatExecutor extends Executor {
    @Resource
    protected JwtTokenUtil jwtTokenUtil;
    @Resource
    protected UserService userService;
    @Resource
    protected UserCache userCache;
    @Resource
    private Snowflake snowflake;
    @Resource
    protected GroupCache groupCache;
    @Resource
    protected GroupService groupService;
    @Resource
    protected RabbitTemplateService rabbitTemplateService;

    /**
     * 执行方法
     * 需要被子类重写
     *
     * @param param   参数
     * @param channel 用户channel
     */
    @Override
    public void execute(JSONObject param, Channel channel) {

    }

    /**
     * 创建一条单聊消息
     *
     * @param toUserId   toUserId
     * @param fromUserId fromUserId
     * @param content    content
     * @param type       type
     * @return SingleChat
     */
    protected SingleChat createNewSingleChat(Integer toUserId, Integer fromUserId, String content, Byte type) {
        SingleChat chat = new SingleChat();
        chat.setId(snowflake.nextIdStr());
        chat.setContent(content);
        chat.setFromUserId(fromUserId);
        chat.setToUserId(toUserId);
        chat.setType(type);
        chat.setCreateTimes(System.currentTimeMillis());
        return chat;
    }
}
