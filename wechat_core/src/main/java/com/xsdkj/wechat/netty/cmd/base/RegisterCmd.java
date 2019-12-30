package com.xsdkj.wechat.netty.cmd.base;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.xsdkj.wechat.bo.SessionBo;
import com.xsdkj.wechat.common.Cmd;
import com.xsdkj.wechat.common.JsonResult;
import com.xsdkj.wechat.common.ResultCodeEnum;
import com.xsdkj.wechat.constant.Attributes;
import com.xsdkj.wechat.constant.ParamConstant;
import com.xsdkj.wechat.constant.UserConstant;
import com.xsdkj.wechat.entity.chat.User;
import com.xsdkj.wechat.service.ex.UnAuthorizedException;
import com.xsdkj.wechat.service.ex.ValidateException;
import com.xsdkj.wechat.netty.cmd.CmdAnno;
import com.xsdkj.wechat.util.JwtTokenUtil;
import com.xsdkj.wechat.util.SessionUtil;
import com.xsdkj.wechat.vo.GroupVo;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

import static com.xsdkj.wechat.constant.ParamConstant.KEY_USER_ID;


/**
 * 注册用户channel
 *
 * @author tiankong
 * @date 2019/11/17 20:00
 */
@Service
@Slf4j
@CmdAnno(cmd = Cmd.REGISTER)
public class RegisterCmd extends AbstractChatCmd {
    @Resource
    private JwtTokenUtil jwtTokenUtil;
    @Resource
    private UserDetailsService userDetailsService;
    @Value("${jwt.tokenHead}")
    private String tokenHead;

    @Override
    protected void parseParam(JSONObject param) {
        try {
            String token = param.getString(ParamConstant.KEY_TOKEN);
            if (ObjectUtil.isEmpty(token)) {
                throw new ValidateException();
            }
            requestParam.setToken(token);
            Integer userId = param.getInteger(KEY_USER_ID);
            if (ObjectUtil.isEmpty(userId)) {
                throw new ValidateException();
            }
            requestParam.setUserId(userId);
        } catch (Exception e) {
            throw new ValidateException();
        }
    }

    @Override
    protected void concreteAction(Channel channel) throws RuntimeException {
        boolean tokenState;
        try {
            String token = requestParam.getToken().substring(tokenHead.length() + 1);
            tokenState = jwtTokenUtil.validateToken(token, userDetailsService.loadUserByUsername(jwtTokenUtil.getUserNameFormToken(token)));
        } catch (Exception e) {
            // 获取token 异常
            remove(channel);
            throw new ValidateException();
        }
        // token过期
        if (!tokenState) {
            throw new UnAuthorizedException();
        }
        Integer userId = requestParam.getUserId();
        if (checkOnline(userId, channel)) {
            sendMessage(channel, JsonResult.failed(ResultCodeEnum.USER_LOGGED_IN, cmd));
            remove(channel);
            return;
        }
        User user = userService.getRedisUserByUserId(userId);
        if (user == null) {
            throw new UnAuthorizedException();
        }
        channel.attr(Attributes.SESSION).set(new SessionBo(user.getId(), user.getUsername(), user.getIcon(), user.getPlatformId(),user.getType()));
        SessionUtil.registerUserChannel(userId, channel);
        sendMessage(channel, JsonResult.success("您已连接成功!", cmd));
        // 加入群聊
        List<GroupVo> userGroupList = groupService.listGroupByUid(userId);
        for (GroupVo group : userGroupList) {
            SessionUtil.getChannelGroup(group.getGid()).add(channel);
            log.info("用户{}已进入群聊,群房间名为{}", user.getUsername(), group.getGroupName());
        }
        log.info("用户{} 已登记到在线用户表,当前在线人数为:{}", user.getUsername(), SessionUtil.ONLINE_USER_MAP.size());
        // TODO 查看有无好友申请消息
        // TODO 查看有无离线单聊消息
        user.setLoginState(UserConstant.LOGGED);
        userService.updateRedisDataByUid(user);
    }

    /**
     * 检查用户是否在线
     *
     * @param userId  用户ID
     * @param channel 用户channel
     * @return boolean
     */
    private boolean checkOnline(Integer userId, Channel channel) {
        SessionBo session = channel.attr(Attributes.SESSION).get();
        if (session != null && session.getUid().equals(userId)) {
            return true;
        } else if (session != null) {
            SessionUtil.ONLINE_USER_MAP.remove(session.getUid());
        }
        Channel channel1 = SessionUtil.ONLINE_USER_MAP.get(userId);
        return channel1 != null;
    }
}
