package com.xsdkj.wechat.netty.cmd.group;

import com.alibaba.fastjson.JSONObject;
import com.xsdkj.wechat.bo.SessionBo;
import com.xsdkj.wechat.common.Cmd;
import com.xsdkj.wechat.common.JsonResult;
import com.xsdkj.wechat.service.ex.UserNotInGroupException;
import com.xsdkj.wechat.netty.cmd.CmdAnno;
import com.xsdkj.wechat.netty.cmd.base.AbstractChatCmd;
import com.xsdkj.wechat.util.SessionUtil;
import com.xsdkj.wechat.vo.RemoveChatVo;
import io.netty.channel.Channel;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

/**
 * @author tiankong
 * @date 2019/12/18 11:09
 */
@Service
@CmdAnno(cmd = Cmd.QUIT_GROUP)
public class QuitGroupCmd extends AbstractChatCmd {
    @Override
    protected void parseParam(JSONObject param) throws Exception {
        parseGroupId(param);
    }

    @Override
    protected void concreteAction(Channel channel) {
        // 通知该群所有用户退群
        Integer groupId = requestParam.getGroupId();
        if (!SessionUtil.GROUP_MAP.get(groupId).getChannelGroup().contains(channel)) {
            throw new UserNotInGroupException();
        }
        RemoveChatVo removeChatVo = createNewRemoveChatVo(groupId, session);
        SessionUtil.quitGroup(groupId, channel);
        sendGroupMessage(groupId, JsonResult.success(removeChatVo, cmd));
        // 通知用户退群成功
        sendMessage(channel, JsonResult.success(cmd));
        // 数据库删除用户与群组关系
        Set<Integer> ids = new HashSet<>();
        ids.add(session.getUid());
        groupService.quitGroup(ids, groupId);
        groupService.updateGroupCount(-ids.size(), groupId);
        // 更新群组redis数据
        groupService.updateRedisGroupById(groupId);
        //更新群组用户redis数据
        userService.updateRedisDataByUid(session.getUid());
    }

    private RemoveChatVo createNewRemoveChatVo(Integer groupId, SessionBo session) {
        RemoveChatVo removeChatVo = new RemoveChatVo();
        removeChatVo.setCount(groupService.getGroupById(groupId).getMembersCount() - 1);
        String nickname = groupService.getNicknameByGroupIdAndUid(groupId, session.getUid());
        removeChatVo.setMessage(nickname + "已退出群聊");
        return removeChatVo;
    }
}
