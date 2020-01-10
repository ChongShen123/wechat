package com.xsdkj.wechat.netty.cmd.group;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.xsdkj.wechat.common.Cmd;
import com.xsdkj.wechat.common.JsonResult;
import com.xsdkj.wechat.netty.cmd.CmdAnno;
import com.xsdkj.wechat.netty.cmd.base.AbstractChatCmd;
import com.xsdkj.wechat.util.LogUtil;
import com.xsdkj.wechat.util.SessionUtil;
import com.xsdkj.wechat.util.ThreadUtil;
import com.xsdkj.wechat.vo.GroupVo;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 查看用户所有群
 *
 * @author tiankong
 * @date 2019/12/15 12:27
 */
@Slf4j
@CmdAnno(cmd = Cmd.LIST_USER_GROUP)
@Service
public class ListUserGroupsCmd extends AbstractChatCmd {

    @Override
    protected void parseParam(JSONObject param) {
    }

    @Override
    protected void concreteAction(Channel channel) {
        log.debug("开始查询用户所有群组...");
        long begin = System.currentTimeMillis();
        List<GroupVo> list = new ArrayList<>(userService.getRedisDataByUid(SessionUtil.getSession(channel).getUid()).getUserGroupRelationMap().values());
        Map<String, Object> map = new HashMap<>(2);
        map.put("groupList", list);
        map.put("count", list.size());
        sendMessage(channel, JsonResult.success(map, cmd));
        log.debug("处理完成 {}ms", DateUtil.spendMs(begin));
        log.debug(LogUtil.INTERVAL);
    }
}
