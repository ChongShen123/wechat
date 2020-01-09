package com.xsdkj.wechat.netty.cmd.group;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.xsdkj.wechat.common.Cmd;
import com.xsdkj.wechat.common.JsonResult;
import com.xsdkj.wechat.netty.cmd.CmdAnno;
import com.xsdkj.wechat.netty.cmd.base.AbstractChatCmd;
import com.xsdkj.wechat.ex.PermissionDeniedException;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 设置用户禁言
 *
 * @author tiankong
 * @date 2019/12/28 16:19
 */
@Slf4j
@CmdAnno(cmd = Cmd.NO_SAY)
@Service
public class SetNoSayCmd extends AbstractChatCmd {
    @Override
    protected void parseParam(JSONObject param) {
        parseGroupId(param);
        parseUserId(param);
        parseTimes(param);
    }

    @Override
    protected void concreteAction(Channel channel) throws RuntimeException {
        log.debug("开始处理用户禁言...");
        long begin = System.currentTimeMillis();
        try {
            Integer groupId = requestParam.getGroupId();
            Integer userId = requestParam.getUserId();
            Long times = requestParam.getTimes();
            if (!checkAdmin(groupId, session.getUid())) {
                log.debug("用户权限不足");
                throw new PermissionDeniedException();
            }
            groupService.saveNoSay(userId, groupId, times);
            groupService.updateRedisNoSayData();
            sendMessage(channel, JsonResult.success(cmd));
        } finally {
            log.debug("用户禁言处理完成 {}ms", DateUtil.spendMs(begin));
        }
    }


}
