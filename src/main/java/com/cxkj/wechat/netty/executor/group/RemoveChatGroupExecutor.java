package com.cxkj.wechat.netty.executor.group;

import com.alibaba.fastjson.JSONObject;
import com.cxkj.wechat.bo.SessionBo;
import com.cxkj.wechat.constant.Command;
import com.cxkj.wechat.constant.SystemConstant;
import com.cxkj.wechat.entity.Group;
import com.cxkj.wechat.entity.SingleChat;
import com.cxkj.wechat.entity.UserGroupRelation;
import com.cxkj.wechat.netty.ex.ValidateException;
import com.cxkj.wechat.netty.executor.ExecutorAnno;
import com.cxkj.wechat.netty.executor.base.ChatExecutor;
import com.cxkj.wechat.util.JsonResult;
import com.cxkj.wechat.util.SessionUtil;
import com.cxkj.wechat.vo.RemoveChatVo;
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
@ExecutorAnno(command = Command.REMOVE_CHAT_GROUP)
public class RemoveChatGroupExecutor extends ChatExecutor {

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
        Group group = groupService.getGroupById(groupId);
        // 通知该群所有在线用户
        RemoveChatVo removeChatVo = createNewRemoveChatVo(ids, groupId, SessionUtil.getSession(channel));
        sendGroupMessage(groupId, JsonResult.success(removeChatVo, command));
        // 删除用户与群关系
        groupService.quitGroup(ids, groupId);
        groupService.updateGroupCount(-ids.size(), groupId);
        // 通知被移除用户
        ids.forEach(uid -> {
            SingleChat newSingleChat = createNewSingleChat(uid, SystemConstant.SYSTEM_USER_ID, "您已退出【" + group.getName() + "】群聊", SystemConstant.QUIT_GROUP);
            Channel userChannel = SessionUtil.ONLINE_USER_MAP.get(uid);
            if (userChannel != null) {
                sendMessage(userChannel, JsonResult.success(newSingleChat));
                SessionUtil.quitGroup(groupId, userChannel);
                newSingleChat.setRead(true);
            } else {
                newSingleChat.setRead(false);
            }
            rabbitTemplateService.addChatInfo(SystemConstant.SINGLE_CHAT_QUEUE_ONE, newSingleChat);
        });


        // 返回信息
        sendMessage(channel, JsonResult.success(command));

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
