package com.xsdkj.wechat.netty.cmd.friend;

import com.alibaba.fastjson.JSONObject;
import com.xsdkj.wechat.common.Cmd;
import com.xsdkj.wechat.common.JsonResult;
import com.xsdkj.wechat.common.ResultCodeEnum;
import com.xsdkj.wechat.constant.ChatConstant;
import com.xsdkj.wechat.constant.ParamConstant;
import com.xsdkj.wechat.entity.chat.Friend;
import com.xsdkj.wechat.entity.chat.FriendApplication;
import com.xsdkj.wechat.netty.cmd.CmdAnno;
import com.xsdkj.wechat.netty.cmd.base.AbstractChatCmd;
import com.xsdkj.wechat.util.SessionUtil;
import com.xsdkj.wechat.vo.FriendApplicationVo;
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
@CmdAnno(cmd = Cmd.FRIEND_AGREE)
public class AgreeFriendCmd extends AbstractChatCmd {

    @Override
    protected void parseParam(JSONObject param) {
        String id = param.getString(ParamConstant.KEY_ID);
        Byte state = param.getByte(ParamConstant.KEY_STATE);
        if (!(state == ChatConstant.AGREE || state == ChatConstant.REFUSE)) {
            throw new RuntimeException();
        }
        requestParam.setId(id);
        requestParam.setState(state);
    }

    @Override
    protected void concreteAction(Channel channel) {
        Byte state = requestParam.getState();
        String id = requestParam.getId();
        FriendApplication application = friendApplicationService.getFriendApplicationById(id);
        if (application == null) {
            log.error("好友请求消息未找到");
            sendMessage(channel, JsonResult.failed(ResultCodeEnum.DATA_NOT_EXIST));
            return;
        }
        if (application.getState() != ChatConstant.UNTREATED) {
            sendMessage(channel, JsonResult.failed(ResultCodeEnum.REPEAT_EXCEPTION, cmd));
            return;
        }
        String msg;
        switch (state) {
            case ChatConstant.AGREE:
                application.setState(ChatConstant.AGREE);
                // 添加好友表
                List<Friend> friends = createFriend(application);
                friendService.saveList(friends);
                msg = ChatConstant.RETURN_MESSAGE_SUCCESS;
                friends.forEach(friend -> userService.updateRedisDataByUid(friend.getUid()));
                break;
            // 回复好友
            case ChatConstant.REFUSE:
                application.setState(ChatConstant.REFUSE);
                msg = ChatConstant.RETURN_MESSAGE_REFUSE;
                break;
            default:
                sendMessage(channel, JsonResult.failed(cmd));
                log.error("错误的好友申请消息");
                return;
        }
        // 回应添邀请方
        Channel fromUserChannel = SessionUtil.ONLINE_USER_MAP.get(application.getFromUserId());
        FriendApplication returnApplication = createFriendApplication(SessionUtil.getSession(channel), application.getFromUserId(), msg, ChatConstant.RETURN_FRIEND);
        if (fromUserChannel != null) {
            returnApplication.setRead(true);
            sendMessage(fromUserChannel, JsonResult.success(new FriendApplicationVo(returnApplication), cmd));
        } else {
            // 设置消息是否被阅读
            returnApplication.setRead(false);
        }
        friendApplicationService.save(returnApplication);
        application.setModifiedTime(System.currentTimeMillis());
        application.setRead(true);
        friendApplicationService.update(application);
        sendMessage(channel, JsonResult.success(cmd));
    }


    private List<Friend> createFriend(FriendApplication application) {
        List<Friend> list = new ArrayList<>();
        list.add(createFriend(application.getToUserId(), application.getFromUserId()));
        list.add(createFriend(application.getFromUserId(), application.getToUserId()));
        return list;
    }

    private Friend createFriend(Integer toUserId, Integer fromUserId) {
        Friend friend = new Friend();
        friend.setUid(toUserId);
        friend.setFid(fromUserId);
        friend.setState(ChatConstant.AGREE);
        long currentTimeMillis = System.currentTimeMillis();
        friend.setModifiedTimes(currentTimeMillis);
        friend.setCreateTimes(currentTimeMillis);
        return friend;
    }
}
