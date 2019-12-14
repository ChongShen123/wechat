package com.cxkj.wechat.netty.executor.friend;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.cxkj.wechat.bo.Session;
import com.cxkj.wechat.constant.Command;
import com.cxkj.wechat.constant.ResultCodeEnum;
import com.cxkj.wechat.constant.SystemConstant;
import com.cxkj.wechat.entity.FriendApplication;
import com.cxkj.wechat.entity.User;
import com.cxkj.wechat.netty.executor.ExecutorAnno;
import com.cxkj.wechat.netty.executor.base.FriendExecutor;
import com.cxkj.wechat.util.JsonResult;
import com.cxkj.wechat.util.SessionUtil;
import com.cxkj.wechat.vo.FriendApplicationVo;
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
public class AddFriendCommand extends FriendExecutor {

    @Override
    public void execute(JSONObject param, Channel channel) {
        String username;
        String message;
        Session session = SessionUtil.getSession(channel);
        try {
            username = param.getString(SystemConstant.KEY_USERNAME);
            message = param.getString(SystemConstant.KEY_MESSAGE);
            if (StrUtil.isNotBlank(username) && session.getUsername().equals(username)) {
                sendMessage(channel, JsonResult.failed(command));
                return;
            }
        } catch (Exception e) {
            sendMessage(channel, JsonResult.failed(ResultCodeEnum.VALIDATE_FAILED, command));
            return;
        }
        User friend = userCache.getByUsername(username);
        if (friend == null) {
            sendMessage(channel, JsonResult.failed(ResultCodeEnum.USER_NOT_FOND, command));
            return;
        }
        if (friendApplicationService.countByToUserIdAndFromUserId(friend.getId(), session.getUserId()) > 0) {
            sendMessage(channel, JsonResult.failed(ResultCodeEnum.REPEAT_EXCEPTION));
            return;
        }
        FriendApplication application = getFriendApplication(session, friend.getId(), message, SystemConstant.ADD_FRIEND);
        Channel friendChannel = SessionUtil.getUserChannel(userCache.getByUsername(username).getId());
        if (friendChannel != null) {
            sendMessage(friendChannel, JsonResult.success(new FriendApplicationVo(application), com.cxkj.wechat.constant.Command.ADD_FRIEND));
        }
        application.setRead(friendChannel != null);
        friendApplicationService.save(application);
        sendMessage(channel, JsonResult.success(SystemConstant.MSG_SUCCESS, command));
    }
}
