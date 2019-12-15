package com.cxkj.wechat.netty.executor.friend;

import com.cxkj.wechat.bo.RequestParamBo;
import com.cxkj.wechat.bo.Session;
import com.cxkj.wechat.constant.Command;
import com.cxkj.wechat.constant.ResultCodeEnum;
import com.cxkj.wechat.constant.SystemConstant;
import com.cxkj.wechat.entity.Friend;
import com.cxkj.wechat.entity.FriendApplication;
import com.cxkj.wechat.netty.executor.ExecutorAnno;
import com.cxkj.wechat.netty.executor.base.ChatExecutor;
import com.cxkj.wechat.util.JsonResult;
import com.cxkj.wechat.util.SessionUtil;
import com.cxkj.wechat.vo.FriendApplicationVO;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 同意或拒绝好友申请
 *
 * @author tiankong
 * @date 2019/12/12 15:49
 */
@Service
@Slf4j
@ExecutorAnno(command = Command.FRIEND_AGREE)
public class AgreeFriendCommand extends ChatExecutor {

    @Override
    protected void concreteAction(RequestParamBo param, Channel channel) {
        FriendApplication application = friendApplicationService.getById(param.getId());
        if (application == null) {
            sendMessage(channel, JsonResult.failed(ResultCodeEnum.DATA_NOT_EXIST));
            return;
        }
        if (application.getState() != SystemConstant.UNTREATED) {
            sendMessage(channel, JsonResult.failed(ResultCodeEnum.REPEAT_EXCEPTION, command));
            return;
        }
        switch (param.getState()) {
            case SystemConstant.AGREE:
                application.setState(SystemConstant.AGREE);
                // 添加好友表
                List<Friend> friends = getFriend(application);
                friendService.saveList(friends);
                break;
            // 回复好友
            case SystemConstant.REFUSE:
                application.setState(SystemConstant.REFUSE);
                break;
            default:
                sendMessage(channel, JsonResult.failed(command));
                log.error("错误的好友申请消息");
                return;
        }
        // 回应添邀请方
        Channel fromUserChannel = SessionUtil.ONLINE_USER_MAP.get(application.getFromUserId());
        FriendApplication returnApplication = getFriendApplication(SessionUtil.getSession(channel), application.getFromUserId(), SystemConstant.RETURN_MESSAGE, SystemConstant.RETURN_FRIEND);
        if (fromUserChannel != null) {
            returnApplication.setRead(true);
            sendMessage(fromUserChannel, JsonResult.success(new FriendApplicationVO(returnApplication), command));
        } else {
            // 设置消息是否被阅读
            returnApplication.setRead(false);
        }
        friendApplicationService.save(returnApplication);
        application.setModifiedTime(System.currentTimeMillis());
        application.setRead(true);
        friendApplicationService.update(application);
    }



    private List<Friend> getFriend(FriendApplication application) {
        List<Friend> list = new ArrayList<>();
        list.add(getFriend(application.getToUserId(), application.getFromUserId()));
        list.add(getFriend(application.getFromUserId(), application.getToUserId()));
        return list;
    }

    private Friend getFriend(Integer toUserId, Integer fromUserId) {
        Friend friend = new Friend();
        friend.setUid(toUserId);
        friend.setFid(fromUserId);
        friend.setState(SystemConstant.AGREE);
        long currentTimeMillis = System.currentTimeMillis();
        friend.setModifiedTimes(currentTimeMillis);
        friend.setCreateTimes(currentTimeMillis);
        return friend;
    }
}
