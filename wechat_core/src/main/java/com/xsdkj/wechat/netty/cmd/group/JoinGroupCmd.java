package com.xsdkj.wechat.netty.cmd.group;

import com.alibaba.fastjson.JSONObject;
import com.xsdkj.wechat.common.Cmd;
import com.xsdkj.wechat.common.JsonResult;
import com.xsdkj.wechat.entity.user.UserGroup;
import com.xsdkj.wechat.ex.GroupNotFoundException;
import com.xsdkj.wechat.ex.UserJoinedException;
import com.xsdkj.wechat.netty.cmd.CmdAnno;
import com.xsdkj.wechat.netty.cmd.base.AbstractChatCmd;
import com.xsdkj.wechat.util.SessionUtil;
import io.netty.channel.Channel;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * @author tiankong
 * @date 2019/12/15 18:55
 */
@CmdAnno(cmd = Cmd.JOIN_GROUP)
@Service
public class JoinGroupCmd extends AbstractChatCmd {

    @Override
    protected void parseParam(JSONObject param) {
        parseIdsAndGroupId(param);
    }

    @Override
    protected void concreteAction(Channel channel) {
        Set<Integer> ids = requestParam.getIds();
        Integer groupId = requestParam.getGroupId();
        // 判断群是否存在
        UserGroup group = groupService.getGroupById(groupId);
        if (group == null) {
            throw new GroupNotFoundException();
        }
        if (checkGroupUserJoined(ids, requestParam.getGroupId())) {
            throw new UserJoinedException();
        }
        ids.forEach(uid -> {
            Channel userChannel = SessionUtil.getUserChannel(uid);
            if (userChannel != null) {
                SessionUtil.joinGroup(groupId, userChannel);
            }
        });
        // 保存用户与群组关系
        groupService.insertUserIds(ids, groupId);
        // 群成员个数增加
        groupService.updateGroupCount(ids.size(), groupId);
        //给用户发送一个入群消息,保存到数据库
        sendCreateGroupMessageToUsers(ids, group);
        // 更新群组redis缓存
        groupService.updateRedisGroupById(groupId);
        sendMessage(channel, JsonResult.success(cmd));
    }
}
