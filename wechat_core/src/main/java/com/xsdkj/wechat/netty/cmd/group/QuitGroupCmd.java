package com.xsdkj.wechat.netty.cmd.group;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.xsdkj.wechat.bo.SessionBo;
import com.xsdkj.wechat.common.Cmd;
import com.xsdkj.wechat.common.JsonResult;
import com.xsdkj.wechat.ex.UserNotInGroupException;
import com.xsdkj.wechat.netty.cmd.CmdAnno;
import com.xsdkj.wechat.netty.cmd.base.AbstractChatCmd;
import com.xsdkj.wechat.util.LogUtil;
import com.xsdkj.wechat.util.SessionUtil;
import com.xsdkj.wechat.vo.RemoveChatVo;
import io.netty.channel.Channel;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

/**
 * @author tiankong
 * @date 2019/12/18 11:09
 */
@Slf4j
@Service
@CmdAnno(cmd = Cmd.QUIT_GROUP)
public class QuitGroupCmd extends AbstractChatCmd {
    @Override
    protected void parseParam(JSONObject param) {
        parseGroupId(param);
    }

    @Override
    protected void concreteAction(Channel channel) {
        long begin = System.currentTimeMillis();
        log.debug("开始处理退群业务...");
        Integer groupId = requestParam.getGroupId();
        if (!SessionUtil.GROUP_MAP.get(groupId).getChannelGroup().contains(channel)) {
            throw new UserNotInGroupException();
        }
        RemoveChatVo removeChatVo = createNewRemoveChatVo(groupId, session);
        SessionUtil.quitGroup(groupId, channel);
        log.debug("用户在内存中退出群聊 {}ms", DateUtil.spendMs(begin));
        sendGroupMessage(groupId, JsonResult.success(removeChatVo, cmd));
        log.debug("用户退群广播 {}ms", DateUtil.spendMs(begin));
        sendMessage(channel, JsonResult.success(cmd));
        log.debug("通知用户退群成功 {}ms", DateUtil.spendMs(begin));
        Set<Integer> ids = new HashSet<>();
        ids.add(session.getUid());
        groupService.quitGroup(ids, groupId);
        log.debug("数据库删除用户与群组关系 {}ms", DateUtil.spendMs(begin));
        groupService.updateGroupCount(-ids.size(), groupId);
        log.debug("群组人数减少 {}ms", DateUtil.spendMs(begin));
        groupService.updateRedisGroupById(groupId);
        log.debug("更新群组缓存 {}ms", DateUtil.spendMs(begin));
        userService.updateRedisDataByUid(session.getUid(), "QuitGroupCmd.concreteAction() 用户退群更新缓存");
        log.debug("更新群组用户数据,退群处理完毕 {}ms", DateUtil.spendMs(begin));
        log.debug(LogUtil.INTERVAL);
    }

    private RemoveChatVo createNewRemoveChatVo(Integer groupId, SessionBo session) {
        RemoveChatVo removeChatVo = new RemoveChatVo();
        removeChatVo.setCount(groupService.getGroupById(groupId).getMembersCount() - 1);
        String nickname = groupService.getNicknameByGroupIdAndUid(groupId, session.getUid());
        removeChatVo.setMessage(nickname + "已退出群聊");
        return removeChatVo;
    }
}
