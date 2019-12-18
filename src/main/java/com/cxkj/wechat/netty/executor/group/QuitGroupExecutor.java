package com.cxkj.wechat.netty.executor.group;

import com.alibaba.fastjson.JSONObject;
import com.cxkj.wechat.bo.SessionBo;
import com.cxkj.wechat.constant.Command;
import com.cxkj.wechat.netty.ex.UserNotInGroup;
import com.cxkj.wechat.netty.executor.ExecutorAnno;
import com.cxkj.wechat.netty.executor.base.ChatExecutor;
import com.cxkj.wechat.util.JsonResult;
import com.cxkj.wechat.util.SessionUtil;
import com.cxkj.wechat.vo.RemoveChatVo;
import io.netty.channel.Channel;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

/**
 * @author tiankong
 * @date 2019/12/18 11:09
 */
@Service
@ExecutorAnno(command = Command.QUIT_GROUP)
public class QuitGroupExecutor extends ChatExecutor {
    @Override
    protected void parseParam(JSONObject param) throws Exception {
        parseGroupId(param);
    }

    @Override
    protected void concreteAction(Channel channel) {
        // 通知该群所有用户退群
        Integer groupId = requestParam.getGroupId();
        if (!SessionUtil.GROUP_MAP.get(groupId).getChannelGroup().contains(channel)) {
            throw new UserNotInGroup();
        }
        RemoveChatVo removeChatVo = createNewRemoveChatVo(groupId, sessionBo);
        SessionUtil.quitGroup(groupId, channel);
        sendGroupMessage(groupId, JsonResult.success(removeChatVo, command));
        // 通知用户退群成功
        sendMessage(channel, JsonResult.success(command));
        // 数据库删除用户与群组关系
        Set<Integer> ids = new HashSet<>();
        ids.add(sessionBo.getUserId());
        groupService.quitGroup(ids, groupId);
        groupService.updateGroupCount(-ids.size(), groupId);
    }

    private RemoveChatVo createNewRemoveChatVo(Integer groupId, SessionBo session) {
        RemoveChatVo removeChatVo = new RemoveChatVo();
        removeChatVo.setCount(groupService.getGroupById(groupId).getMembersCount() - 1);
        String nickname = groupService.getNicknameByGroupIdAndUid(groupId, session.getUserId());
        removeChatVo.setMessage(nickname + "已退出群聊");
        return removeChatVo;
    }
}
