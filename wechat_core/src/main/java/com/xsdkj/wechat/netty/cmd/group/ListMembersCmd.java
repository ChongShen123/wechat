package com.xsdkj.wechat.netty.cmd.group;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.xsdkj.wechat.common.Cmd;
import com.xsdkj.wechat.common.JsonResult;
import com.xsdkj.wechat.netty.cmd.CmdAnno;
import com.xsdkj.wechat.netty.cmd.base.AbstractChatCmd;
import com.xsdkj.wechat.vo.ListMembersVo;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author tiankong
 * @date 2019/12/17 11:12
 */
@Slf4j
@Service
@CmdAnno(cmd = Cmd.LIST_GROUP_MEMBERS)
public class ListMembersCmd extends AbstractChatCmd {
    @Override
    protected void parseParam(JSONObject param) {
        parseGroupId(param);
    }

    @Override
    protected void concreteAction(Channel channel) {
        long begin = System.currentTimeMillis();
        Integer groupId = requestParam.getGroupId();
        List<ListMembersVo> listMembersVos = groupService.listGroupMembersByGroupId(groupId);
        if (listMembersVos.size() > 0) {
            sendMessage(channel, JsonResult.success(listMembersVos, cmd));
        }
        log.debug("查询群成员用户完成 {}ms", DateUtil.spendMs(begin));
    }
}
