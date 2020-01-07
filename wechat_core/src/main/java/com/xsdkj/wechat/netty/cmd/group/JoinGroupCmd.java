package com.xsdkj.wechat.netty.cmd.group;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.xsdkj.wechat.common.Cmd;
import com.xsdkj.wechat.common.JsonResult;
import com.xsdkj.wechat.entity.user.UserGroup;
import com.xsdkj.wechat.ex.GroupNotFoundException;
import com.xsdkj.wechat.ex.UserJoinedException;
import com.xsdkj.wechat.netty.cmd.CmdAnno;
import com.xsdkj.wechat.netty.cmd.base.AbstractChatCmd;
import com.xsdkj.wechat.util.LogUtil;
import com.xsdkj.wechat.util.SessionUtil;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Set;

/**
 * @author tiankong
 * @date 2019/12/15 18:55
 */
@Slf4j
@CmdAnno(cmd = Cmd.JOIN_GROUP)
@Service
public class JoinGroupCmd extends AbstractChatCmd {

    @Override
    protected void parseParam(JSONObject param) {
        parseIdsAndGroupId(param);
    }

    @Override
    protected void concreteAction(Channel channel) {
        long begin = System.currentTimeMillis();
        log.debug("开始处理加入群聊...");
        Set<Integer> ids = requestParam.getIds();
        Integer groupId = requestParam.getGroupId();
        log.debug("要加入群组的用户id{},群组id:{} {}ms", ids, groupId, DateUtil.spendMs(begin));
        // 判断群是否存在
        UserGroup group = groupService.getGroupById(groupId);
        if (group != null) {
            if (!checkGroupUserJoined(ids, requestParam.getGroupId())) {
                ids.forEach(uid -> {
                    Channel userChannel = SessionUtil.getUserChannel(uid);
                    if (userChannel != null) {
                        SessionUtil.joinGroup(groupId, userChannel);
                    }
                });
                //
                groupService.insertUserIds(ids, groupId);
                log.debug("已保存用户与群组关系到数据库 {}ms", DateUtil.spendMs(begin));
                // 群成员个数增加
                groupService.updateGroupCount(ids.size(), groupId);
                log.debug("已更新群成员个数 {}ms", DateUtil.spendMs(begin));
                //给用户发送一个入群消息,保存到数据库
                sendCreateGroupMessageToUsers(ids, group);
                // 更新群组redis缓存
                groupService.updateRedisGroupById(groupId);
                sendMessage(channel, JsonResult.success(cmd));
                log.debug("加入群组处理完成 {}ms", DateUtil.spendMs(begin));
                log.debug(LogUtil.INTERVAL);
                return;
            }
            log.error("有用户已加入群组 {}ms", DateUtil.spendMs(begin));
            throw new UserJoinedException();
        }
        log.error("{}群不存在 {}ms", groupId, DateUtil.spendMs(begin));
        throw new GroupNotFoundException();
    }
}
