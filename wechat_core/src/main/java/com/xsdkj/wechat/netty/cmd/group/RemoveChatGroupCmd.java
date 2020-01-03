package com.xsdkj.wechat.netty.cmd.group;

import com.alibaba.fastjson.JSONObject;
import com.xsdkj.wechat.bo.RabbitMessageBoxBo;
import com.xsdkj.wechat.bo.SessionBo;
import com.xsdkj.wechat.common.Cmd;
import com.xsdkj.wechat.common.JsonResult;
import com.xsdkj.wechat.constant.SystemConstant;
import com.xsdkj.wechat.constant.ChatConstant;
import com.xsdkj.wechat.constant.RabbitConstant;
import com.xsdkj.wechat.entity.chat.SingleChat;
import com.xsdkj.wechat.entity.chat.UserGroup;
import com.xsdkj.wechat.netty.cmd.CmdAnno;
import com.xsdkj.wechat.netty.cmd.base.AbstractChatCmd;
import com.xsdkj.wechat.service.ex.PermissionDeniedException;
import com.xsdkj.wechat.service.ex.ValidateException;
import com.xsdkj.wechat.util.SessionUtil;
import com.xsdkj.wechat.vo.RemoveChatVo;
import io.netty.channel.Channel;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author tiankong
 * @date 2019/12/17 14:09
 */
@Service
@CmdAnno(cmd = Cmd.REMOVE_CHAT_GROUP)
public class RemoveChatGroupCmd extends AbstractChatCmd {

    @Override
    protected void parseParam(JSONObject param) {
        parseIdsAndGroupId(param);
    }

    @Override
    protected void concreteAction(Channel channel) {
        // 检查要移除的用户是否合法
        Set<Integer> ids = requestParam.getIds();
        Integer groupId = requestParam.getGroupId();
        if (!checkGroupUserJoined(ids, groupId)) {
            throw new ValidateException();
        }
        UserGroup group = groupService.getGroupById(groupId);
        if (!checkAdmin(groupId, session.getUid())) {
            throw new PermissionDeniedException();
        }
        // 通知该群所有在线用户
        RemoveChatVo removeChatVo = createNewRemoveChatVo(ids, groupId, SessionUtil.getSession(channel));
        sendGroupMessage(groupId, JsonResult.success(removeChatVo, cmd));
        // 删除用户与群关系
        groupService.quitGroup(ids, groupId);
        groupService.updateGroupCount(-ids.size(), groupId);
        // 通知被移除用户
        ids.forEach(uid -> {
            SingleChat newSingleChat = createNewSingleChat(uid, SystemConstant.SYSTEM_USER_ID, "您已退出【" + group.getName() + "】群聊", ChatConstant.QUIT_GROUP);
            Channel userChannel = SessionUtil.ONLINE_USER_MAP.get(uid);
            if (userChannel != null) {
                sendMessage(userChannel, JsonResult.success(newSingleChat));
                SessionUtil.quitGroup(groupId, userChannel);
                newSingleChat.setRead(true);
            } else {
                newSingleChat.setRead(false);
            }
            // 更新被移除用户的redis数据
            userService.updateRedisDataByUid(uid);
            rabbitTemplateService.addExchange(RabbitConstant.FANOUT_CHAT_NAME, RabbitMessageBoxBo.createBox(RabbitConstant.BOX_TYPE_SINGLE_CHAT, newSingleChat));
        });
        // 更新群组redis数据
        groupService.updateRedisGroupById(groupId);
        // 返回信息
        sendMessage(channel, JsonResult.success(cmd));

    }

    private RemoveChatVo createNewRemoveChatVo(Set<Integer> ids, Integer groupId, SessionBo session) {
        RemoveChatVo chatVo = new RemoveChatVo();
        List<String> usernameList = new ArrayList<>();
        chatVo.setCount(groupService.getGroupById(groupId).getMembersCount() - ids.size());
        ids.forEach(uid -> {
            String nickname = groupService.getNicknameByGroupIdAndUid(groupId, uid);
            if (nickname != null) {
                usernameList.add(nickname);
            }
        });
        StringBuilder sb = new StringBuilder();
        sb.append(session.getUsername()).append("已将");
        for (int i = 0; i < usernameList.size(); i++) {
            sb.append(usernameList.get(i));
            if (i < usernameList.size() - 1) {
                sb.append("、");
            }
        }
        sb.append("移出群聊");
        chatVo.setMessage(sb.toString());
        return chatVo;
    }
}
