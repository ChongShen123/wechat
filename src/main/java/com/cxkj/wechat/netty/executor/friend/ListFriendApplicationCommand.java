package com.cxkj.wechat.netty.executor.friend;

import com.alibaba.fastjson.JSONObject;
import com.cxkj.wechat.constant.Command;
import com.cxkj.wechat.entity.FriendApplication;
import com.cxkj.wechat.netty.executor.ExecutorAnno;
import com.cxkj.wechat.netty.executor.base.FriendExecutor;
import com.cxkj.wechat.util.JsonResult;
import com.cxkj.wechat.util.SessionUtil;
import io.netty.channel.Channel;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 查看好友申请列表
 *
 * @author tiankong
 * @date 2019/12/12 17:26
 */
@Service
@ExecutorAnno(command = Command.LIST_FRIEND_APPLICATION)
public class ListFriendApplicationCommand extends FriendExecutor {
    @Override
    public void execute(JSONObject param, Channel channel) {
        List<FriendApplication> friendApplicationList = friendApplicationService.listByUserId(SessionUtil.getSession(channel).getUserId());
        sendMessage(channel, JsonResult.success(friendApplicationList, command));
    }
}
