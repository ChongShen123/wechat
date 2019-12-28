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
            requestParam.setByteType(type);
        } catch (Exception e) {
            throw new ValidateException();
        }
    }

    @Override
    protected void concreteAction(Channel channel) {
        Long times = groupService.getNoSayTimesByUidAndGroupId(session.getUid(), requestParam.getGroupId());
        System.out.println(times);
        if (times != null) {
            long noSayTimes = times - System.currentTimeMillis();
            if (noSayTimes > 0) {
                sendMessage(channel, JsonResult.failed(String.format("请在%s秒后发言", noSayTimes / 1000), cmd));
                return;
            } else if (times == -1) {
                sendMessage(channel, JsonResult.failed(ResultCodeEnum.NO_SAY_EXCEPTION, cmd));
                return;
            }
        }
//         构建一个群聊消息
        GroupChat groupChat = createNewGroupChat(requestParam.getByteType(), requestParam.getGroupId(), requestParam.getContent(), SessionUtil.getSession(channel));
//         将消息发送给群在线所有用户
        sendGroupMessage(requestParam.getGroupId(), JsonResult.success(groupChat, cmd));
        RabbitMessageBoxBo box = RabbitMessageBoxBo.createBox(SystemConstant.BOX_TYPE_GROUP_CHAT, groupChat);
        rabbitTemplateService.addExchange(SystemConstant.FANOUT_CHAT_NAME, box);
    }

    public static void main(String[] args) {
        System.out.println(System.currentTimeMillis() + 20 * 1000);
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
