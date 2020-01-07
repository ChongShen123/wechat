package com.xsdkj.wechat.netty.cmd.group;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.xsdkj.wechat.bo.MsgBox;
import com.xsdkj.wechat.bo.SessionBo;
import com.xsdkj.wechat.common.Cmd;
import com.xsdkj.wechat.common.JsonResult;
import com.xsdkj.wechat.constant.SystemConstant;
import com.xsdkj.wechat.constant.ChatConstant;
import com.xsdkj.wechat.constant.RabbitConstant;
import com.xsdkj.wechat.entity.chat.SingleChat;
import com.xsdkj.wechat.entity.user.UserGroup;
import com.xsdkj.wechat.netty.cmd.CmdAnno;
import com.xsdkj.wechat.netty.cmd.base.AbstractChatCmd;
import com.xsdkj.wechat.ex.PermissionDeniedException;
import com.xsdkj.wechat.ex.ValidateException;
import com.xsdkj.wechat.util.LogUtil;
import com.xsdkj.wechat.util.SessionUtil;
import com.xsdkj.wechat.vo.RemoveChatVo;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 移出群聊
 *
 * @author tiankong
 * @date 2019/12/17 14:09
 */
@Slf4j
@Service
@CmdAnno(cmd = Cmd.REMOVE_CHAT_GROUP)
public class RemoveChatGroupCmd extends AbstractChatCmd {

    @Override
    protected void parseParam(JSONObject param) {
        parseIdsAndGroupId(param);
    }

    @Override
    protected void concreteAction(Channel channel) {
        long begin = System.currentTimeMillis();
        log.debug("开始处理群组移除工作...");
        Set<Integer> ids = requestParam.getIds();
        Integer groupId = requestParam.getGroupId();
        log.debug("检查要移除的用户是否在该群");
        if (checkGroupUserJoined(ids, groupId)) {
            UserGroup group = groupService.getGroupById(groupId);
            if (checkAdmin(groupId, session.getUid())) {
                // 通知该群所有在线用户
                RemoveChatVo removeChatVo = createNewRemoveChatVo(ids, groupId, SessionUtil.getSession(channel));
                log.debug("通知该群在线用户被移除用户信息 {}ms", DateUtil.spendMs(begin));
                sendGroupMessage(groupId, JsonResult.success(removeChatVo, cmd));
                log.debug("删除用户与群关系 {}ms", DateUtil.spendMs(begin));
                groupService.quitGroup(ids, groupId);
                log.debug("修改群成员人数 {}ms", DateUtil.spendMs(begin));
                groupService.updateGroupCount(-ids.size(), groupId);
                //
                log.debug("通知被移除用户...");
                ids.forEach(uid -> {
                    SingleChat newSingleChat = chatUtil.createNewSingleChat(uid, SystemConstant.SYSTEM_USER_ID, "您已退出【" + group.getName() + "】群聊", ChatConstant.QUIT_GROUP);
                    Channel userChannel = SessionUtil.ONLINE_USER_MAP.get(uid);
                    if (userChannel != null) {
                        sendMessage(userChannel, JsonResult.success(newSingleChat));
                        SessionUtil.quitGroup(groupId, userChannel);
                        newSingleChat.setRead(true);
                    } else {
                        newSingleChat.setRead(false);
                    }
                    userService.updateRedisDataByUid(uid, "RemoveChatGroupCmd.concreteAction()");
                    log.debug("更新被移除用户{}缓存数据 {}ms", uid, DateUtil.spendMs(begin));
                    singleChatService.save(newSingleChat);
                    log.debug("保存被移除用户{}退群消息到数据库 {}ms", uid, DateUtil.spendMs(begin));
                });
                groupService.updateRedisGroupById(groupId);
                sendMessage(channel, JsonResult.success(cmd));
                log.debug("移除群组处理完成 {}ms", DateUtil.spendMs(begin));
                log.debug(LogUtil.INTERVAL);
                return;
            }
            log.error("用户无相关权限");
            log.debug(LogUtil.INTERVAL);
            throw new PermissionDeniedException();
        }
        log.error("部分用户不在该群组");
        log.debug(LogUtil.INTERVAL);
        throw new ValidateException();
    }

    private RemoveChatVo createNewRemoveChatVo(Set<Integer> ids, Integer groupId, SessionBo session) {
        RemoveChatVo chatVo = new RemoveChatVo();
        List<String> usernameList = new ArrayList<>();
        chatVo.setCount(groupService.getGroupById(groupId).getMembersCount() - ids.size());
        ids.forEach(uid -> {
            String nickname = groupService.getNicknameByGroupIdAndUid(groupId, uid);
            if (nickname != null) {
                usernameList.add(nickname);
            }
        });
        StringBuilder sb = new StringBuilder();
        sb.append(session.getUsername()).append("已将");
        for (int i = 0; i < usernameList.size(); i++) {
            sb.append(usernameList.get(i));
            if (i < usernameList.size() - 1) {
                sb.append("、");
            }
        }
        sb.append("移出群聊");
        chatVo.setMessage(sb.toString());
        return chatVo;
    }
}
