package com.cxkj.wechat.netty.executor.group;

import com.alibaba.fastjson.JSONObject;
import com.cxkj.wechat.constant.Command;
import com.cxkj.wechat.entity.Group;
import com.cxkj.wechat.netty.ex.GroupNotFoundException;
import com.cxkj.wechat.netty.ex.UserJoinedException;
import com.cxkj.wechat.netty.executor.ExecutorAnno;
import com.cxkj.wechat.netty.executor.base.ChatExecutor;
import com.cxkj.wechat.util.JsonResult;
import com.cxkj.wechat.util.SessionUtil;
import io.netty.channel.Channel;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * @author tiankong
 * @date 2019/12/15 18:55
 */
@ExecutorAnno(command = Command.JOIN_GROUP)
@Service
public class JoinGroup extends ChatExecutor {

    @Override
    protected void parseParam(JSONObject param) {
        parseIdsAndGroupId(param);
    }

    @Override
    protected void concreteAction(Channel channel) {
        Set<Integer> ids = requestParam.getIds();
        Integer groupId = requestParam.getGroupId();
        // 判断群是否存在
        Group group = groupService.getGroupById(groupId);
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
        sendMessage(channel, JsonResult.success(command));
    }


}
