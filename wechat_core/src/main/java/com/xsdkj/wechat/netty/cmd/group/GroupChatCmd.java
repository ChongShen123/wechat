package com.xsdkj.wechat.netty.cmd.group;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.xsdkj.wechat.bo.MsgBox;
import com.xsdkj.wechat.bo.SessionBo;
import com.xsdkj.wechat.common.Cmd;
import com.xsdkj.wechat.common.JsonResult;
import com.xsdkj.wechat.constant.GroupConstant;
import com.xsdkj.wechat.constant.ParamConstant;
import com.xsdkj.wechat.constant.RabbitConstant;
import com.xsdkj.wechat.entity.chat.GroupChat;
import com.xsdkj.wechat.entity.user.UserGroup;
import com.xsdkj.wechat.ex.NoSayException;
import com.xsdkj.wechat.netty.cmd.CmdAnno;
import com.xsdkj.wechat.netty.cmd.base.AbstractChatCmd;
import com.xsdkj.wechat.ex.BannedChatException;
import com.xsdkj.wechat.ex.ValidateException;
import com.xsdkj.wechat.service.GroupChatService;
import com.xsdkj.wechat.service.UserGroupService;
import com.xsdkj.wechat.util.LogUtil;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 群聊
 *
 * @author tiankong
 * @date 2019/12/17 11:52
 */
@Slf4j
@Service
@CmdAnno(cmd = Cmd.GROUP_CHAT)
public class GroupChatCmd extends AbstractChatCmd {
    @Resource
    private GroupChatService groupChatService;

    @Override
    protected void parseParam(JSONObject param) {
        try {
            Integer groupId = param.getInteger(ParamConstant.KEY_GROUP_ID);
            requestParam.setGroupId(groupId);
            String content = param.getString(ParamConstant.KEY_CONTENT);
            requestParam.setContent(content);
            Byte type = param.getByte(ParamConstant.KEY_TYPE);
            requestParam.setByteType(type);
        } catch (Exception e) {
            throw new ValidateException();
        }
    }

    @Override
    protected void concreteAction(Channel channel) {
        long begin = System.currentTimeMillis();
        log.debug("开始处理群聊业务...");
        Integer groupId = requestParam.getGroupId();
        UserGroup group = groupService.getGroupById(groupId);
        if (group.getNoSayType().equals(GroupConstant.GROUP_NO_SAY)) {
            throw new BannedChatException();
        }
        // 检查用户是否被禁言
        checkUserNoSay(channel, groupId, begin);
        // 构建一个群聊消息
        GroupChat groupChat = createNewGroupChat(requestParam.getByteType(), groupId, requestParam.getContent(), session);
        log.debug("创建一个群聊消息{} {}ms", groupChat, DateUtil.spendMs(begin));
        // 将消息发送给群在线所有用户
        sendGroupMessage(groupId, JsonResult.success(groupChat, cmd));
        log.debug("已将消息发送给本群在线用户 {}ms", DateUtil.spendMs(begin));
        groupChatService.save(groupChat);
        log.debug("已将消息保存到数据库,群聊处理完成 {}ms", DateUtil.spendMs(begin));
        log.debug(LogUtil.INTERVAL);
    }

    /**
     * 检查用户是否被禁言
     *
     * @param channel 用户 channel
     * @param groupId 群id
     * @param begin   计时
     */
    private void checkUserNoSay(Channel channel, Integer groupId, long begin) {
        log.debug("检查用户在该群是否被禁言...");
        Long times = groupService.getNoSayTimesByUidAndGroupId(session.getUid(), groupId);
        if (times != null) {
            long noSayTimes = times - System.currentTimeMillis();
            log.debug("禁言时间为:{} {}ms", noSayTimes, DateUtil.spendMs(begin));
            // 用户被禁言 noSayTimes ms
            if (noSayTimes > 0) {
                Map<String, Long> map = new HashMap<>(1);
                map.put("relieveTimes", noSayTimes);
                sendMessage(channel, JsonResult.failed(map, cmd));
                log.debug("禁言剩余时间{}ms", noSayTimes);
                return;
            }
            if (times == -1) {
                log.debug("用户被永久禁言 {}ms", DateUtil.spendMs(begin));
                throw new NoSayException();
            }
            groupService.relieveNoSay(session.getUid(), groupId);
            groupService.updateRedisNoSayData();
            log.debug("用户禁言时间已过,删除禁言记录 {}ms", DateUtil.spendMs(begin));
        }
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
