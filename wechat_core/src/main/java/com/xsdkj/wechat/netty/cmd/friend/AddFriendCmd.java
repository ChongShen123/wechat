package com.xsdkj.wechat.netty.cmd.friend;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.xsdkj.wechat.bo.SessionBo;
import com.xsdkj.wechat.common.Cmd;
import com.xsdkj.wechat.common.JsonResult;
import com.xsdkj.wechat.constant.ChatConstant;
import com.xsdkj.wechat.constant.ParamConstant;
import com.xsdkj.wechat.entity.chat.FriendApplication;
import com.xsdkj.wechat.entity.user.User;
import com.xsdkj.wechat.ex.AlreadyFriendException;
import com.xsdkj.wechat.ex.DataEmptyException;
import com.xsdkj.wechat.ex.RepeatException;
import com.xsdkj.wechat.netty.cmd.CmdAnno;
import com.xsdkj.wechat.netty.cmd.base.AbstractChatCmd;
import com.xsdkj.wechat.ex.ValidateException;
import com.xsdkj.wechat.service.FriendApplicationService;
import com.xsdkj.wechat.util.SessionUtil;
import com.xsdkj.wechat.vo.FriendApplicationVo;
import com.xsdkj.wechat.vo.UserFriendVo;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author tiankong
 * @date 2019/12/12 13:22
 */
@Service
@Slf4j
@CmdAnno(cmd = Cmd.ADD_FRIEND)
public class AddFriendCmd extends AbstractChatCmd {
    @Resource
    private FriendApplicationService friendApplicationService;

    @Override
    protected void parseParam(JSONObject param) {
        log.debug("开始解析参数...");
        String username = param.getString(ParamConstant.KEY_USERNAME);
        String content = param.getString(ParamConstant.KEY_CONTENT);
        if (StrUtil.isBlank(username) || StrUtil.isBlank(content)) {
            throw new ValidateException();
        }
        log.debug("username:{}", username);
        log.debug("content:{}", content);
        requestParam.setUsername(username);
        requestParam.setContent(content);
    }

    @Override
    protected void concreteAction(Channel channel) {
        long begin = System.currentTimeMillis();
        log.debug("开始处理添加好友工作...");
        String username = requestParam.getUsername();
        String content = requestParam.getContent();
        User friend = userService.getByUsername(username);
        if (friend == null) {
            log.error("好友信息为空:{}", username);
            throw new DataEmptyException();
        }
        Channel friendChannel = SessionUtil.getUserChannel(friend.getId());
        FriendApplication application = handleAddFriend(channel, content, friend.getId(), friendChannel != null);
        if (friendChannel != null) {
            sendMessage(friendChannel, JsonResult.success(new FriendApplicationVo(application), Cmd.ADD_FRIEND));
        }
        log.debug("{}是否在线:{}", username, friendChannel != null);
        log.debug("添加好友消息处理完毕 {}ms", DateUtil.spendMs(begin));
    }

    /**
     * 处理用户添加好友
     *
     * @param channel 当前用户
     * @param content 正文
     * @param fid     朋友id
     * @param isRead  消息是否为已读
     * @return FriendApplication
     */
    private FriendApplication handleAddFriend(Channel channel, String content, Integer fid, boolean isRead) {
        long begin = System.currentTimeMillis();
        List<UserFriendVo> userFriendVos = userService.getRedisDataByUid(session.getUid()).getUserFriendVos();
        for (UserFriendVo userFriendVo : userFriendVos) {
            if (userFriendVo.getUid().equals(fid)) {
                log.error("{}用户已为好友", fid);
                throw new AlreadyFriendException();
            }
        }
        if (friendApplicationService.countByToUserIdAndFromUserId(fid, session.getUid()) == 0) {
            FriendApplication application = createFriendApplication(session, fid, content, ChatConstant.ADD_FRIEND);
            application.setRead(isRead);
            friendApplicationService.save(application);
            sendMessage(channel, JsonResult.success(ChatConstant.MSG_SUCCESS, cmd));
            log.debug("好友申请已保存数据库 {}ms", DateUtil.spendMs(begin));
            return application;
        }
        log.error("重复的操作");
        throw new RepeatException();
    }
}
