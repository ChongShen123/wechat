package com.xsdkj.wechat.netty.cmd.group;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSONObject;
import com.xsdkj.wechat.bo.GroupInfoBo;
import com.xsdkj.wechat.bo.SessionBo;
import com.xsdkj.wechat.common.Cmd;
import com.xsdkj.wechat.common.JsonResult;
import com.xsdkj.wechat.common.ResultCodeEnum;
import com.xsdkj.wechat.entity.chat.Group;
import com.xsdkj.wechat.entity.chat.User;
import com.xsdkj.wechat.netty.cmd.CmdAnno;
import com.xsdkj.wechat.netty.cmd.base.BaseChatCmd;
import com.xsdkj.wechat.util.QrUtil;
import com.xsdkj.wechat.util.SessionUtil;
import com.xsdkj.wechat.vo.CreateGroupVo;
import io.netty.channel.Channel;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author tiankong
 * @date 2019/12/14 14:34
 */
@Service
@CmdAnno(cmd = Cmd.CREATE_GROUP)
@Slf4j
public class CreateGroupCmd extends BaseChatCmd {
    @Value("${file.root-path}")
    private String root;
    @Value("${file.group-path}")
    private String groupPath;
    @Resource
    private QrUtil qrUtil;

    @Override
    protected void parseParam(JSONObject param) {
        parseIdsAndGroupId(param);
    }

    @Override
    protected void concreteAction(Channel channel) {
        Set<Integer> ids = requestParam.getIds();
        if (!checkUser(ids)) {
            sendMessage(channel, JsonResult.failed(ResultCodeEnum.VALIDATE_FAILED, cmd));
            return;
        }
        // 加入当前用户ID
        ids.add(session.getUid());
        // 获取一个群
        Group group = createNewGroup(SessionUtil.getSession(channel), ids);
        // 保存用户与群组关系
        groupService.insertUserIds(ids, group.getId());
        // 群成员个数添加
        groupService.updateGroupCount(ids.size(), group.getId());
        // 回复用户创建群成功
        sendMessage(channel, JsonResult.success(new CreateGroupVo(group), cmd));
        // 给用户发送一个入群消息,保存到数据库
        sendCreateGroupMessageToUsers(ids, group);
        // 将在线用户添加到 channelGroup
        DefaultChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
        GroupInfoBo groupInfo = new GroupInfoBo(group, channelGroup);
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
    private boolean checkUser(Set<Integer> ids) {
        for (Integer id : ids) {
            User user = userService.getByUserId(id);
            if (user == null) {
                return false;
            }
        }
        return true;
    }


    /**
     * 创建一个群
     *
     * @param session 创建者信息
     * @param ids     群成员
     * @return 群
     */
    private Group createNewGroup(SessionBo session, Set<Integer> ids) {
        Group group = new Group();
        group.setName(generateGroupName(ids));
        group.setIcon(getRandomIcon(root, groupPath));
        group.setOwnerId(session.getUid());
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
    private String generateGroupName(Set<Integer> ids) {
        Set<User> threeUsers = new HashSet<>();
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
    private String getRandomIcon(String root, String path) {
        List<String> list = FileUtil.listFileNames(root + path);
        return groupPath + RandomUtil.randomEle(list);
    }
}
