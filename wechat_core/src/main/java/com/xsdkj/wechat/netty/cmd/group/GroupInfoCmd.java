package com.xsdkj.wechat.netty.cmd.group;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.xsdkj.wechat.common.Cmd;
import com.xsdkj.wechat.common.JsonResult;
import com.xsdkj.wechat.netty.cmd.CmdAnno;
import com.xsdkj.wechat.netty.cmd.base.AbstractChatCmd;
import com.xsdkj.wechat.vo.GroupInfoVo;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 查看群详情
 *
 * @author tiankong
 * @date 2019/12/15 14:31
 */
@Slf4j
@CmdAnno(cmd = Cmd.GROUP_INFO)
@Service
public class GroupInfoCmd extends AbstractChatCmd {

    @Override
    protected void parseParam(JSONObject param) {
        parseGroupId(param);
    }

    @Override
    protected void concreteAction(Channel channel) {
        long begin = System.currentTimeMillis();
        GroupInfoVo groupInfo = groupService.getGroupInfo(requestParam.getGroupId());
        sendMessage(channel, JsonResult.success(groupInfo, cmd));
        log.debug("查看群详情完成 {}ms", DateUtil.spendMs(begin));
    }
}
