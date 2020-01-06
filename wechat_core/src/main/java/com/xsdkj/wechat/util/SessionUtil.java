package com.xsdkj.wechat.util;

import cn.hutool.core.date.DateUtil;
import com.xsdkj.wechat.bo.GroupInfoBo;
import com.xsdkj.wechat.bo.SessionBo;
import com.xsdkj.wechat.constant.Attributes;
import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author tiankong
 * @date 2019/11/17 19:22
 */
@Slf4j
@Component
public class SessionUtil {
    /**
     * 握手实例
     */
    public static Map<String, WebSocketServerHandshaker> WEB_SOCKET_SERVER_HAND_SHAKER = new ConcurrentHashMap<>();

    /**
     * 所有群
     */
    public static Map<Integer, GroupInfoBo> GROUP_MAP = new ConcurrentHashMap<>();

    /**
     * 在线用户
     */
    public static Map<Integer, Channel> ONLINE_USER_MAP = new ConcurrentHashMap<>();

    // -------------------------------------基本----------------------------------------------------

    /**
     * 获取用户channel
     */
    public static Channel getUserChannel(Integer uid) {
        return ONLINE_USER_MAP.get(uid);
    }

    /**
     * 注册用户channel
     */
    public static void registerUserChannel(Integer uid, Channel channel) {
        long begin = System.currentTimeMillis();
        log.debug("注册channel用户ID:{} ", uid);
        ONLINE_USER_MAP.put(uid, channel);
        log.debug("用户channel注册完毕,当前在线用户人数{}用时{}ms", ONLINE_USER_MAP.size(), DateUtil.spendMs(begin));
    }

    public static SessionBo getSession(Channel channel) {
        return channel.attr(Attributes.SESSION).get();
    }

    // --------------------------------群相关-----------------------------------

    /**
     * 加入群组
     */
    public static void joinGroup(Integer groupId, Channel channel) {
        GROUP_MAP.get(groupId).getChannelGroup().add(channel);
    }

    /**
     * 退出群组
     */
    public static void quitGroup(Integer groupId, Channel channel) {
        GROUP_MAP.get(groupId).getChannelGroup().remove(channel);
    }

    /**
     * 获取群组ChannelGroup
     */
    public static ChannelGroup getChannelGroup(Integer groupId) {
        return GROUP_MAP.get(groupId).getChannelGroup();
    }


    /**
     * 获取群组用户ID集合
     */
    public static List<Integer> listGroupMemberIds(Integer groupId) {
        List<Integer> list = new ArrayList<>();
        getChannelGroup(groupId).forEach(channel -> {
            if (channel != null) {
                list.add(getSession(channel).getUid());
            }
        });
        return list;
    }

}
