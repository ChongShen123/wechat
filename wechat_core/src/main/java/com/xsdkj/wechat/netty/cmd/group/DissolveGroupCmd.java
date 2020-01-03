package com.xsdkj.wechat.netty.cmd.group;

import com.alibaba.fastjson.JSONObject;
import com.xsdkj.wechat.common.Cmd;
import com.xsdkj.wechat.common.JsonResult;
import com.xsdkj.wechat.entity.user.UserGroup;
import com.xsdkj.wechat.netty.cmd.CmdAnno;
import com.xsdkj.wechat.netty.cmd.base.AbstractChatCmd;
import com.xsdkj.wechat.service.ex.PermissionDeniedException;
import com.xsdkj.wechat.util.SessionUtil;
import com.xsdkj.wechat.vo.ListMembersVo;
import io.netty.channel.Channel;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 解散群组
 *
 * @author tiankong
 * @date 2019/12/28 12:57
 */
@CmdAnno(cmd = Cmd.DISSOLVE_GROUP)
@Service
public class DissolveGroupCmd extends AbstractChatCmd {
    @Override
    protected void parseParam(JSONObject param) throws Exception {
        parseGroupId(param);
    }

    @Override
    protected void concreteAction(Channel channel) throws RuntimeException {
        Integer groupId = requestParam.getGroupId();
        UserGroup group = groupService.getGroupById(groupId);
        // 只有群主可以解散该群
        if (!session.getUid().equals(group.getOwnerId())) {
            throw new PermissionDeniedException();
        }
        // 查询该群组的所有成员
        List<ListMembersVo> listMembersVos = groupService.listGroupMembersByGroupId(groupId);
        // 通知该群所有群成员退群消息
        sendGroupMessage(groupId, JsonResult.success(String.format("%s群组已解散.", groupService.getGroupById(groupId).getName())));
        // 物理删除
        groupService.deleteById(groupId);
        // 内存删除
        SessionUtil.GROUP_MAP.remove(groupId);
        // 缓存删除
        groupService.deleteRedisData(groupId);
        // 更新用户缓存
        listMembersVos.forEach(member -> userService.updateRedisDataByUid(member.getUid()));
    }
}
