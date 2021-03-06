package com.cxkj.wechat.netty.executor.group;

import com.alibaba.fastjson.JSONObject;
import com.cxkj.wechat.bo.SessionBo;
import com.cxkj.wechat.constant.SystemConstant;
import com.cxkj.wechat.bo.RequestParamBo;
import com.cxkj.wechat.constant.Command;
import com.cxkj.wechat.entity.GroupChat;
import com.cxkj.wechat.netty.ex.ValidateException;
import com.cxkj.wechat.netty.executor.ExecutorAnno;
import com.cxkj.wechat.netty.executor.base.ChatExecutor;
import com.cxkj.wechat.util.JsonResult;
import com.cxkj.wechat.util.SessionUtil;
import io.netty.channel.Channel;
import org.springframework.stereotype.Service;

/**
 * 群聊
 *
 * @author tiankong
 * @date 2019/12/17 11:52
 */
@Service
@ExecutorAnno(command = Command.GROUP_CHAT)
public class GroupChatExecutor extends ChatExecutor {
    @Override
    protected void parseParam(JSONObject param) {
        try {
            Integer groupId = param.getInteger(SystemConstant.KEY_GROUP_ID);
            String content = param.getString(SystemConstant.KEY_CONTENT);
            Byte type = param.getByte(SystemConstant.KEY_TYPE);
            requestParam.setGroupId(groupId);
            requestParam.setContent(content);
            requestParam.setType(type);
        } catch (Exception e) {
            throw new ValidateException();
        }
    }

    @Override
    protected void concreteAction(Channel channel) {
        // 构建一个群聊消息
        GroupChat groupChat = createNewGroupChat(requestParam.getType(), requestParam.getGroupId(), requestParam.getContent(), SessionUtil.getSession(channel));
        // 将消息发送给群在线所有用户
        sendGroupMessage(requestParam.getGroupId(), JsonResult.success(groupChat, command));
        // 将消息加入到消息队列
        rabbitTemplateService.addChatInfo(SystemConstant.GROUP_CHAT_QUEUE_ONE, groupChat);
    }

    /**
     * 创建一个群聊消息
     *
     * @param type    0信息 1语音 2图片 3撤销
     * @param groupId 群ID
     * @param content 消息内容
     * @param session 发送者信息
     * @return GroupChat
     */
    private GroupChat createNewGroupChat(Byte type, Integer groupId, String content, SessionBo session) {
        GroupChat groupChat = new GroupChat();
        groupChat.setType(type);
        groupChat.setToGroupId(groupId);
        groupChat.setContent(content);
        groupChat.setFromUserId(session.getUserId());
        groupChat.setId(snowflake.nextIdStr());
        groupChat.setCreateTimes(System.currentTimeMillis());
        return groupChat;
    }


}
