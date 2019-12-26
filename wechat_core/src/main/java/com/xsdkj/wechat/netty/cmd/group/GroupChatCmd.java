package com.xsdkj.wechat.netty.cmd.group;

import com.alibaba.fastjson.JSONObject;
import com.xsdkj.wechat.bo.RabbitMessageBox;
import com.xsdkj.wechat.bo.SessionBo;
import com.xsdkj.wechat.common.Cmd;
import com.xsdkj.wechat.common.JsonResult;
import com.xsdkj.wechat.common.SystemConstant;
import com.xsdkj.wechat.entity.chat.GroupChat;
import com.xsdkj.wechat.netty.ex.ValidateException;
import com.xsdkj.wechat.netty.cmd.CmdAnno;
import com.xsdkj.wechat.netty.cmd.base.BaseChatCmd;
import com.xsdkj.wechat.util.SessionUtil;
import io.netty.channel.Channel;
import org.springframework.stereotype.Service;

/**
 * 群聊
 *
 * @author tiankong
 * @date 2019/12/17 11:52
 */
@Service
@CmdAnno(cmd = Cmd.GROUP_CHAT)
public class GroupChatCmd extends BaseChatCmd {
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
        sendGroupMessage(requestParam.getGroupId(), JsonResult.success(groupChat, cmd));
        // TODO 使用到RabbitMQ
        rabbitTemplateService.addChatInfo(SystemConstant.FANOUT_CHAT_NAME, RabbitMessageBox.createBox(SystemConstant.BOX_TYPE_GROUP_CHAT, groupChat));
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
        groupChat.setFromUserId(session.getUid());
        groupChat.setId(snowflake.nextIdStr());
        groupChat.setCreateTimes(System.currentTimeMillis());
        return groupChat;
    }


}
