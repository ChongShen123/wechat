package com.xsdkj.wechat.netty.cmd.group;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.xsdkj.wechat.common.Cmd;
import com.xsdkj.wechat.common.JsonResult;
import com.xsdkj.wechat.netty.cmd.CmdAnno;
import com.xsdkj.wechat.netty.cmd.base.AbstractChatCmd;
import com.xsdkj.wechat.vo.GroupAdminVo;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author tiankong
 * @date 2020/1/8 12:32
 */
@Slf4j
@CmdAnno(cmd = Cmd.LIST_GROUP_ADMINS)
@Component
public class ListAdminCmd extends AbstractChatCmd {
    @Override
    protected void parseParam(JSONObject param) throws Exception {
        parseGroupId(param);
    }

    @Override
    protected void concreteAction(Channel channel) throws RuntimeException {
        long begin = System.currentTimeMillis();
        Integer groupId = requestParam.getGroupId();
        List<GroupAdminVo> list = groupService.listGroupAdmins(groupId);
        sendMessage(channel, JsonResult.success(list, cmd));
        log.debug("用户{}查看群所有管理员完毕 {}ms",session.getUid(), DateUtil.spendMs(begin));

    }
}
