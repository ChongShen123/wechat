package com.xsdkj.wechat.netty.cmd.group;

import com.alibaba.fastjson.JSONObject;
import com.xsdkj.wechat.common.Cmd;
import com.xsdkj.wechat.common.JsonResult;
import com.xsdkj.wechat.constant.GroupConstant;
import com.xsdkj.wechat.entity.user.UserGroup;
import com.xsdkj.wechat.netty.cmd.CmdAnno;
import com.xsdkj.wechat.netty.cmd.base.AbstractChatCmd;
import com.xsdkj.wechat.service.ex.DataEmptyException;
import com.xsdkj.wechat.service.ex.PermissionDeniedException;
import com.xsdkj.wechat.service.ex.RepetitionException;
import com.xsdkj.wechat.service.ex.ValidateException;
import io.netty.channel.Channel;
import org.springframework.stereotype.Service;

/**
 * 设置群管理员
 *
 * @author tiankong
 * @date 2019/12/28 17:46
 */
@CmdAnno(cmd = Cmd.SET_GROUP_MANAGER)
@Service
public class SetGroupManagerCmd extends AbstractChatCmd {
    @Override
    protected void parseParam(JSONObject param) {
        parseGroupId(param);
        parseUserId(param);
        parseIntegerType(param);
    }

    @Override
    protected void concreteAction(Channel channel) throws RuntimeException {
        // 群id
        Integer groupId = requestParam.getGroupId();
        // 管理员id
        Integer userId = requestParam.getUserId();
        UserGroup group = groupService.getGroupById(groupId);
        if (group == null) {
            throw new DataEmptyException();
        }
        // 只有群主可以管理员
        if (!group.getOwnerId().equals(session.getUid())) {
            throw new PermissionDeniedException();
        }
        Integer type = requestParam.getIntType();
        switch (type) {
            // 添加管理员
            case GroupConstant.ADD_MANAGER:
                Integer count = groupService.countGroupManger(group.getId(), userId);
                if (count > 0) {
                    throw new RepetitionException();
                }
                groupService.addGroupManager(group.getId(), userId);
                break;
            // 删除管理员
            case GroupConstant.DELETE_MANAGER:
                count = groupService.countGroupManger(group.getId(), userId);
                if (count < 1) {
                    throw new DataEmptyException();
                }
                groupService.deleteGroupManager(group.getId(), userId);
                break;
            default:
                throw new ValidateException();
        }
        sendMessage(channel, JsonResult.success(cmd));
    }
}
