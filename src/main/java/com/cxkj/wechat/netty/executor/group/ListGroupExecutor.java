package com.cxkj.wechat.netty.executor.group;

import com.cxkj.wechat.bo.RequestParamBo;
import com.cxkj.wechat.constant.Command;
import com.cxkj.wechat.netty.executor.ExecutorAnno;
import com.cxkj.wechat.netty.executor.base.ChatExecutor;
import com.cxkj.wechat.util.JsonResult;
import com.cxkj.wechat.util.SessionUtil;
import com.cxkj.wechat.util.ThreadUtil;
import com.cxkj.wechat.vo.ListGroupVO;
import io.netty.channel.Channel;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author tiankong
 * @date 2019/12/15 12:27
 */
@ExecutorAnno(command = Command.LIST_GROUP)
@Service
public class ListGroupExecutor extends ChatExecutor {

    @Override
    protected void concreteAction(RequestParamBo param, Channel channel) {
        ThreadUtil.getSingleton().submit(() -> {
            List<ListGroupVO> list = groupService.listGroupByUid(SessionUtil.getSession(channel).getUserId());
            Map<String, Object> map = new HashMap<>(2);
            map.put("groupList", list);
            map.put("count", list.size());
            sendMessage(channel, JsonResult.success(map, command));
        });
    }

}
