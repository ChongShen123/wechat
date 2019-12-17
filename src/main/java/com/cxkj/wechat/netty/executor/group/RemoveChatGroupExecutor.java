package com.cxkj.wechat.netty.executor.group;

import com.alibaba.fastjson.JSONObject;
import com.cxkj.wechat.bo.SessionBo;
import com.cxkj.wechat.constant.Command;
import com.cxkj.wechat.entity.UserGroupRelation;
import com.cxkj.wechat.netty.ex.ValidateException;
import com.cxkj.wechat.netty.executor.ExecutorAnno;
import com.cxkj.wechat.netty.executor.base.ChatExecutor;
import com.cxkj.wechat.util.SessionUtil;
import com.cxkj.wechat.vo.RemoveChatVo;
import io.netty.channel.Channel;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
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

        // 删除用户与群关系
        groupService.quitGroup(ids, groupId);
        groupService.updateGroupCount(ids.size(), groupId);
        // 构建一条退群通知
        RemoveChatVo removeChatVo = createNewRemoveChatVo(ids, groupId, SessionUtil.getSession(channel));
        removeChatVo.setCount(groupService.getGroupById(groupId).getMembersCount());

        // 通知被移除用户退群消息
        // 通知该群所有在线用户
        // 通知被移除用户
        // 返回信息
    }

    private RemoveChatVo createNewRemoveChatVo(Set<Integer> ids, Integer groupId, SessionBo session) {
        RemoveChatVo chatVo = new RemoveChatVo();
        List<String> usernameList = new ArrayList<>();
        ids.forEach(item -> {
            UserGroupRelation groupRelation = groupService.getNicknameByGroupIdAndUid(groupId, item);
            if (groupRelation != null) {
                usernameList.add(groupRelation.getGroupNickname());
            }
        });
        StringBuilder sb = new StringBuilder();
        sb.append(session.getUsername()).append("已将");
        for (int i = 0; i < usernameList.size(); i++) {
            sb.append(usernameList.get(i));
            if (i < usernameList.size()) {
                sb.append("、");
            }
        }
        sb.append("已出群聊");
        chatVo.setMessage(sb.toString());
        return chatVo;
    }
}
