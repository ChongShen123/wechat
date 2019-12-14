package com.cxkj.wechat.netty.executor.base;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.cxkj.wechat.bo.Session;
import com.cxkj.wechat.bo.UserGroup;
import com.cxkj.wechat.constant.Attributes;
import com.cxkj.wechat.constant.ResultCodeEnum;
import com.cxkj.wechat.entity.User;
import com.cxkj.wechat.netty.executor.ExecutorAnno;
import com.cxkj.wechat.service.cache.UserCache;
import com.cxkj.wechat.util.JsonResult;
import com.cxkj.wechat.util.JwtTokenUtil;
import com.cxkj.wechat.util.SessionUtil;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


/**
 * 注册用户channel
 *
 * @author tiankong
 * @date 2019/11/17 20:00
 */
@Service
@Slf4j
@ExecutorAnno(command = com.cxkj.wechat.constant.Command.REGISTER)
public class RegisterExecutor extends ChatExecutor {
    @Resource
    private JwtTokenUtil jwtTokenUtil;
    @Resource
    private UserDetailsService userDetailsService;
    @Resource
    private UserCache userCache;
    @Value("${jwt.tokenHead}")
    private String tokenHead;

    @Override
    public void execute(JSONObject param, Channel channel) {
        String token;
        try {
            token = param.getString("token");
            if (StrUtil.isBlank(token)) {
                sendMessage(channel, JsonResult.failed(ResultCodeEnum.VALIDATE_FAILED));
                return;
            }
        } catch (Exception e) {
            sendMessage(channel, JsonResult.failed(ResultCodeEnum.VALIDATE_FAILED));
            return;
        }
        String username;
        try {
            if (!token.startsWith(tokenHead)) {
                throw new RuntimeException();
            }
            token = token.substring(tokenHead.length() + 1);
            username = jwtTokenUtil.getUserNameFormToken(token);
            jwtTokenUtil.validateToken(token, userDetailsService.loadUserByUsername(username));
        } catch (Exception e) {
            sendMessage(channel, JsonResult.failed(ResultCodeEnum.VALIDATE_TOKEN));
            remove(channel);
            return;
        }
        Integer userId = userCache.getByUsername(username).getId();
        if (checkOnline(userId, channel)) {
            sendMessage(channel, JsonResult.failed(ResultCodeEnum.USER_LOGGED_IN, command));
            channel.close();
            return;
        }

        User user = userCache.getByUserId(userId);
        if (user == null) {
            sendMessage(channel, JsonResult.failed(ResultCodeEnum.USER_NOT_FOND, command));
            return;
        }
        SessionUtil.registerUserChannel(userId, channel);
        channel.attr(Attributes.SESSION).set(new Session(user.getId(), user.getUsername(), user.getIcon()));
        sendMessage(channel, JsonResult.success("您已连接成功!", command));
        // 加入群聊
        List<UserGroup> userGroupList = groupCache.listGroupByUid(userId);
        for (UserGroup group : userGroupList) {
            SessionUtil.getChannelGroup(group.getGid()).add(channel);
            log.info("用户{}已进入群聊,群房间名为{}", user.getUsername(), group.getGroupName());
        }
        log.info("用户{} 已登记到在线用户表,当前在线人数为:{}", user.getUsername(), SessionUtil.ONLINE_USER_MAP.size());

        // TODO 查看有无好友申请消息
        // TODO 查看有无离线单聊消息
    }

    /**
     * 检查用户是否在线
     *
     * @param userId  用户ID
     * @param channel 用户channel
     * @return boolean
     */
    private boolean checkOnline(Integer userId, Channel channel) {
        Session session = channel.attr(Attributes.SESSION).get();
        if (session != null && session.getUserId().equals(userId)) {
            return true;
        } else if (session != null) {
            SessionUtil.ONLINE_USER_MAP.remove(session.getUserId());
        }
        return SessionUtil.ONLINE_USER_MAP.get(userId) != null;
    }
}
