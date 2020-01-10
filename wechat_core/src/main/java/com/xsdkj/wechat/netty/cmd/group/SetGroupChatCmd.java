package com.xsdkj.wechat.netty.cmd.group;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.xsdkj.wechat.common.Cmd;
import com.xsdkj.wechat.common.JsonResult;
import com.xsdkj.wechat.constant.GroupConstant;
import com.xsdkj.wechat.entity.user.UserGroup;
import com.xsdkj.wechat.netty.cmd.CmdAnno;
import com.xsdkj.wechat.netty.cmd.base.AbstractChatCmd;
import com.xsdkj.wechat.ex.DataEmptyException;
import com.xsdkj.wechat.ex.PermissionDeniedException;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 设置群是否可聊天
 *
 * @author tiankong
 * @date 2019/12/30 12:37
 */
@Slf4j
@Component
@CmdAnno(cmd = Cmd.SET_GROUP_CHAT)
public class SetGroupChatCmd extends AbstractChatCmd {

    @Override
    protected void parseParam(JSONObject param) throws Exception {
        parseGroupId(param);
        parseIntegerType(param);
    }

    @Override
    protected void concreteAction(Channel channel) throws RuntimeException {
        long begin = System.currentTimeMillis();
        Integer groupId = requestParam.getGroupId();
        Integer type = requestParam.getIntType();
        UserGroup group = groupService.getGroupById(groupId);
        try {
            if (group == null) {
                log.error("群{}数据不存在", groupId);
                throw new DataEmptyException();
            }
            if (!checkAdmin(groupId, session.getUid())) {
                log.error("用户{}没有相关权限", session.getUid());
                throw new PermissionDeniedException();

            }
            groupService.setGroupChat(groupId, type);
            groupService.updateRedisGroupById(groupId);
            sendMessage(channel, JsonResult.success(cmd));
        } finally {
            log.debug("群{}聊天功能已{} {}ms", groupId, Byte.parseByte(type + "") == GroupConstant.GROUP_NO_SAY ? "关闭" : "开启", DateUtil.spendMs(begin));
        }
    }
}
