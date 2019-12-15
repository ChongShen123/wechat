package com.cxkj.wechat.netty.executor.friend;

import com.cxkj.wechat.bo.RequestParamBo;
import com.cxkj.wechat.bo.Session;
import com.cxkj.wechat.constant.Command;
import com.cxkj.wechat.constant.ResultCodeEnum;
import com.cxkj.wechat.constant.SystemConstant;
import com.cxkj.wechat.entity.FriendApplication;
import com.cxkj.wechat.entity.User;
import com.cxkj.wechat.netty.executor.ExecutorAnno;
import com.cxkj.wechat.netty.executor.base.ChatExecutor;
import com.cxkj.wechat.util.JsonResult;
import com.cxkj.wechat.util.SessionUtil;
import com.cxkj.wechat.vo.FriendApplicationVO;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author tiankong
 * @date 2019/12/12 13:22
 */
@Service
@Slf4j
@ExecutorAnno(command = Command.ADD_FRIEND)
public class AddFriendCommand extends ChatExecutor {

    @Override
    protected void concreteAction(RequestParamBo param, Channel channel) {
        Session session = SessionUtil.getSession(channel);
        User friend = userService.getByUsername(param.getUsername());
        if (friend == null) {
            sendMessage(channel, JsonResult.failed(ResultCodeEnum.USER_NOT_FOND, command));
            return;
        }
        if (friendApplicationService.countByToUserIdAndFromUserId(friend.getId(), session.getUserId()) > 0) {
            sendMessage(channel, JsonResult.failed(ResultCodeEnum.REPEAT_EXCEPTION));
            return;
        }
        FriendApplication application = getFriendApplication(session, friend.getId(), param.getMessage(), SystemConstant.ADD_FRIEND);
        Channel friendChannel = SessionUtil.getUserChannel(userService.getByUsername(param.getUsername()).getId());
        if (friendChannel != null) {
            sendMessage(friendChannel, JsonResult.success(new FriendApplicationVO(application), Command.ADD_FRIEND));
        }
        application.setRead(friendChannel != null);
        friendApplicationService.save(application);
        sendMessage(channel, JsonResult.success(SystemConstant.MSG_SUCCESS, command));
    }
}
