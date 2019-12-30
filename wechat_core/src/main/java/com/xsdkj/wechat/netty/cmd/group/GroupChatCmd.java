package com.xsdkj.wechat.netty.cmd.group;

import com.alibaba.fastjson.JSONObject;
import com.xsdkj.wechat.bo.RabbitMessageBoxBo;
import com.xsdkj.wechat.bo.SessionBo;
import com.xsdkj.wechat.common.Cmd;
import com.xsdkj.wechat.common.JsonResult;
import com.xsdkj.wechat.common.ResultCodeEnum;
import com.xsdkj.wechat.common.SystemConstant;
import com.xsdkj.wechat.entity.chat.GroupChat;
import com.xsdkj.wechat.entity.chat.GroupNoSay;
import com.xsdkj.wechat.service.ex.ValidateException;
import com.xsdkj.wechat.netty.cmd.CmdAnno;
import com.xsdkj.wechat.netty.cmd.base.BaseChatCmd;
import com.xsdkj.wechat.util.SessionUtil;
import io.netty.channel.Channel;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

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
            requestParam.setGroupId(param.getInteger(SystemConstant.KEY_GROUP_ID));
            requestParam.setContent(param.getString(SystemConstant.KEY_CONTENT));
            requestParam.setByteType(param.getByte(SystemConstant.KEY_TYPE));
        } catch (Exception e) {
            throw new ValidateException();
        }
    }

    @Override
    protected void concreteAction(Channel channel) {
        Integer groupId = requestParam.getGroupId();
        // 检查用户是否被禁言
        if (checkUserNoSay(channel, groupId)) {
            return;
        }
        // 构建一个群聊消息
        GroupChat groupChat = createNewGroupChat(requestParam.getByteType(), groupId, requestParam.getContent(), session);
        // 将消息发送给群在线所有用户
        sendGroupMessage(groupId, JsonResult.success(groupChat, cmd));
        rabbitTemplateService.addExchange(SystemConstant.FANOUT_CHAT_NAME, RabbitMessageBoxBo.createBox(SystemConstant.BOX_TYPE_GROUP_CHAT, groupChat));
    }

    /**
     * 检查用户是否被禁言
     *
     * @param channel 用户 channel
     * @param groupId 群id
     * @return 返回用户是否被禁言
     */
    private boolean checkUserNoSay(Channel channel, Integer groupId) {
        Long times = groupService.getNoSayTimesByUidAndGroupId(session.getUid(), groupId);
        if (times != null) {
            long noSayTimes = times - System.currentTimeMillis();
            // 用户被禁言 noSayTimes ms
            if (noSayTimes > 0) {
                sendMessage(channel, JsonResult.failed(String.format("请在%s秒后发言", noSayTimes / 1000), cmd));
                return true;
            }
            // 用户被永久禁言
            else if (times == -1) {
                sendMessage(channel, JsonResult.failed(ResultCodeEnum.NO_SAY_EXCEPTION, cmd));
                return true;
            }
            // 用户禁言时间已过,删除禁言记录
            else {
                groupService.relieveNoSay(session.getUid(), groupId);
                groupService.updateRedisNoSayData();
            }
        }
        return false;
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
