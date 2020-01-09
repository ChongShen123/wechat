package com.xsdkj.wechat.netty.cmd.group;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.xsdkj.wechat.common.Cmd;
import com.xsdkj.wechat.common.JsonResult;
import com.xsdkj.wechat.entity.user.UserGroup;
import com.xsdkj.wechat.netty.cmd.CmdAnno;
import com.xsdkj.wechat.netty.cmd.base.AbstractChatCmd;
import com.xsdkj.wechat.ex.PermissionDeniedException;
import com.xsdkj.wechat.util.LogUtil;
import com.xsdkj.wechat.util.SessionUtil;
import com.xsdkj.wechat.vo.ListMembersVo;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 解散群组
 *
 * @author tiankong
 * @date 2019/12/28 12:57
 */
@Slf4j
@CmdAnno(cmd = Cmd.DISSOLVE_GROUP)
@Service
public class DissolveGroupCmd extends AbstractChatCmd {
    @Override
    protected void parseParam(JSONObject param) {
        parseGroupId(param);
    }

    @Override
    protected void concreteAction(Channel channel) throws RuntimeException {
        long begin = System.currentTimeMillis();
        log.debug("开始处理解散群组业务...");
        Integer groupId = requestParam.getGroupId();
        UserGroup group = groupService.getGroupById(groupId);
        log.debug("只有群主可以解散该群");
        if (!session.getUid().equals(group.getOwnerId())) {
            throw new PermissionDeniedException();
        }
        List<ListMembersVo> listMembersVos = groupService.listGroupMembersByGroupId(groupId);
        log.debug("查询该群组的所有成员{} {}ms", listMembersVos.size(), DateUtil.spendMs(begin));
        sendGroupMessage(groupId, JsonResult.success(String.format("%s群组已解散.", groupService.getGroupById(groupId).getName())));
        log.debug("通知该群所有群成员解散群组消息 {}ms", DateUtil.spendMs(begin));
        SessionUtil.GROUP_MAP.remove(groupId);
        log.debug("内存删除 {}ms", DateUtil.spendMs(begin));
        groupService.deleteById(groupId);
        log.debug("物理删除 {}ms", DateUtil.spendMs(begin));
        groupService.deleteRedisData(groupId);
        log.debug("缓存删除 {}ms", DateUtil.spendMs(begin));
        listMembersVos.forEach(member -> userService.updateRedisDataByUid(member.getUid(), "DissolveGroupCmd.解散群组"));
        log.debug("解散群组完毕 {}ms", DateUtil.spendMs(begin));
        log.debug(LogUtil.INTERVAL);
    }
}
