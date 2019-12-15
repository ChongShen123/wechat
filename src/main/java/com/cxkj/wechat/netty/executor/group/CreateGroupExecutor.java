package com.cxkj.wechat.netty.executor.group;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.RandomUtil;
import com.cxkj.wechat.bo.GroupInfo;
import com.cxkj.wechat.bo.RequestParamBo;
import com.cxkj.wechat.bo.Session;
import com.cxkj.wechat.constant.Command;
import com.cxkj.wechat.constant.ResultCodeEnum;
import com.cxkj.wechat.constant.SystemConstant;
import com.cxkj.wechat.entity.Group;
import com.cxkj.wechat.entity.SingleChat;
import com.cxkj.wechat.entity.User;
import com.cxkj.wechat.netty.executor.ExecutorAnno;
import com.cxkj.wechat.netty.executor.base.ChatExecutor;
import com.cxkj.wechat.util.JsonResult;
import com.cxkj.wechat.util.QrUtil;
import com.cxkj.wechat.util.SessionUtil;
import com.cxkj.wechat.vo.CreateGroupVO;
import io.netty.channel.Channel;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author tiankong
 * @date 2019/12/14 14:34
 */
@Service
@ExecutorAnno(command = Command.CREATE_GROUP)
@Slf4j
public class CreateGroupExecutor extends ChatExecutor {
    @Value("${file.root-path}")
    private String root;
    @Value("${file.group-path}")
    private String groupPath;
    @Resource
    private QrUtil qrUtil;

    @Override
    protected void concreteAction(RequestParamBo param, Channel channel) {
        List<Integer> ids = param.getIds();
        if (!checkUser(ids)) {
            sendMessage(channel, JsonResult.failed(ResultCodeEnum.VALIDATE_FAILED, command));
            return;
        }
        // 加入当前用户ID
        ids.add(SessionUtil.getSession(channel).getUserId());
        // 获取一个群
        Group group = createNewGroup(SessionUtil.getSession(channel), ids);
        // 保存用户与群组关系
        groupService.insertUserIds(ids, group.getId());
        // 回复用户创建群成功
        sendMessage(channel, JsonResult.success(new CreateGroupVO(group), command));
        // 给用户发送一个入群消息,保存到数据库
        sendCreateGroupMessageToUsers(ids, group);
        // 将在线用户添加到 channelGroup
        DefaultChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
        GroupInfo groupInfo = new GroupInfo(group, channelGroup);
        ids.forEach(id -> {
            Channel userChannel = SessionUtil.ONLINE_USER_MAP.get(id);
            if (userChannel != null) {
                channelGroup.add(userChannel);
            }
        });
        SessionUtil.GROUP_MAP.put(group.getId(), groupInfo);
    }

    /**
     * 检测添加的用户是否都存在
     *
     * @param ids ids
     * @return boolean
     */
    private boolean checkUser(List<Integer> ids) {
        for (Integer id : ids) {
            User user = userService.getByUserId(id);
            if (user == null) {
                return false;
            }
        }
        return true;
    }

    /**
     * 给用户发送一个入群消息,保存到数据库
     *
     * @param ids   用户ID
     * @param group 群信息
     */
    private void sendCreateGroupMessageToUsers(List<Integer> ids, Group group) {
        List<SingleChat> list = new ArrayList<>();
        ids.forEach(id -> list.add(createNewSingleChat(id, SystemConstant.SYSTEM_USER_ID, "您已加入【" + group.getName() + "】 开始聊天吧", Command.JOIN_GROUP_NOTICE)));
        list.forEach(singleChat -> {
            Channel toUserChannel = SessionUtil.ONLINE_USER_MAP.get(singleChat.getToUserId());
            if (toUserChannel != null) {
                sendMessage(toUserChannel, JsonResult.success(singleChat));
            }
            singleChat.setRead(toUserChannel != null);
            rabbitTemplateService.addSingleChat(SystemConstant.SINGLE_CHAT_QUEUE_ONE, singleChat);
        });
    }

    /**
     * 创建一个群
     *
     * @param session 创建者信息
     * @param ids     群成员
     * @return 群
     */
    private Group createNewGroup(Session session, List<Integer> ids) {
        Group group = new Group();
        group.setName(generateGroupName(ids));
        group.setIcon(getIcon(root, groupPath));
        group.setOwnerId(session.getUserId());
        group.setMembersCount(ids.size());
        group.setState(false);
        group.setIsSave(true);
        long currentTimeMillis = System.currentTimeMillis();
        group.setCreateTimes(currentTimeMillis);
        group.setModifiedTimes(currentTimeMillis);
        groupService.save(group);
        groupService.updateQr(group.getId(), qrUtil.generate("group_" + group.getId()));
        return group;
    }

    /**
     * 生成房间名
     *
     * @param ids 用户id
     * @return 房间名
     */
    private String generateGroupName(List<Integer> ids) {
        List<User> threeUsers = new ArrayList<>();
        List<User> users = userService.listUserByIds(ids);
        for (int i = 0; i < users.size(); i++) {
            threeUsers.add(users.get(i));
            if (i > 3) {
                break;
            }
        }
        StringBuilder sb = new StringBuilder();
        threeUsers.forEach(user -> sb.append(user.getUsername()).append("、"));
        sb.delete(sb.lastIndexOf("、"), sb.length());
        sb.append("的群聊");
        return sb.toString();
    }

    /**
     * 获取指定路径下一个随机文件名
     *
     * @return 一个图片名
     */
    private String getIcon(String root, String path) {
        List<String> list = FileUtil.listFileNames(root + path);
        return groupPath + RandomUtil.randomEle(list);
    }
}
