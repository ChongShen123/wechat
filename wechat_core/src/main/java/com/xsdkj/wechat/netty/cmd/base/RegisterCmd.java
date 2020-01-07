package com.xsdkj.wechat.netty.cmd.base;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.xsdkj.wechat.bo.MsgBox;
import com.xsdkj.wechat.bo.SessionBo;
import com.xsdkj.wechat.bo.UserDetailsBo;
import com.xsdkj.wechat.common.Cmd;
import com.xsdkj.wechat.common.JsonResult;
import com.xsdkj.wechat.constant.Attributes;
import com.xsdkj.wechat.constant.ParamConstant;
import com.xsdkj.wechat.constant.RabbitConstant;
import com.xsdkj.wechat.constant.UserConstant;
import com.xsdkj.wechat.entity.chat.FriendApplication;
import com.xsdkj.wechat.entity.chat.SingleChat;
import com.xsdkj.wechat.entity.user.User;
import com.xsdkj.wechat.ex.UnAuthorizedException;
import com.xsdkj.wechat.ex.UserAlreadyRegister;
import com.xsdkj.wechat.ex.ValidateException;
import com.xsdkj.wechat.netty.cmd.CmdAnno;
import com.xsdkj.wechat.service.FriendApplicationService;
import com.xsdkj.wechat.service.UserGroupService;
import com.xsdkj.wechat.util.JwtTokenUtil;
import com.xsdkj.wechat.util.LogUtil;
import com.xsdkj.wechat.util.SessionUtil;
import com.xsdkj.wechat.vo.GroupVo;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    @Resource
    private UserGroupService userGroupService;
    @Resource
    private FriendApplicationService friendApplicationService;
    @Value("${jwt.tokenHead}")
    private String tokenHead;

    @Override
    protected void parseParam(JSONObject param) {
        log.debug("开始解析参数...");
        long begin = System.currentTimeMillis();
        String token = param.getString(ParamConstant.KEY_TOKEN);
        Integer userId = param.getInteger(KEY_USER_ID);
        log.debug("用户token:{}  {}ms", token, DateUtil.spendMs(begin));
        log.debug("用户id:{}  {}ms", userId, DateUtil.spendMs(begin));
        if (ObjectUtil.isNotEmpty(token) && ObjectUtil.isNotEmpty(userId)) {
            requestParam.setToken(token);
            requestParam.setUserId(userId);
            log.debug("参数解析完成 {}ms", DateUtil.spendMs(begin));
            return;
        }
        throw new ValidateException();
    }

    @Override
    protected void concreteAction(Channel channel) throws RuntimeException {
        log.debug("开始处理注册工作...");
        long begin = System.currentTimeMillis();
        try {
            String token = requestParam.getToken().substring(tokenHead.length() + 1);
            Integer userId = requestParam.getUserId();
            boolean tokenState = jwtTokenUtil.validateToken(token, userDetailsService.loadUserByUsername(jwtTokenUtil.getUserNameFormToken(token)));
            log.debug("token验证状态:{} {}ms", tokenState, DateUtil.spendMs(begin));
            if (tokenState) {
                if (!checkUserOnline(userId, channel)) {
                    UserDetailsBo userDetailsBo = userService.getRedisDataByUid(userId);
                    User user = userDetailsBo.getUser();
                    if (user == null) {
                        log.error("暂未登录或token已过期");
                        throw new UnAuthorizedException();
                    }
                    channel.attr(Attributes.SESSION).set(new SessionBo(user.getId(), user.getUsername(), user.getIcon(), user.getPlatformId(), user.getType()));
                    log.debug("绑定Channel属性完毕 {}ms", DateUtil.spendMs(begin));
                    SessionUtil.registerUserChannel(userId, channel);
                    sendMessage(channel, JsonResult.success("您已连接成功!", cmd));
                    // 加入群聊
                    List<GroupVo> userGroupList = new ArrayList<>(userDetailsBo.getUserGroupRelationMap().values());
                    log.debug("用户群组个数:{} {}ms", userGroupList.size(), DateUtil.spendMs(begin));
                    if (userGroupList.size() > 0) {
                        for (GroupVo group : userGroupList) {
                            SessionUtil.getChannelGroup(group.getGid()).add(channel);
                            log.debug("用户{}已进入群聊,群房间名为{}", user.getUsername(), group.getGroupName());
                        }
                    }
                    log.debug("查看有无好友申请消息...");
                    List<FriendApplication> friendApplications = friendApplicationService.listByReadAndUserId(false, userId);
                    if (friendApplications.size() > 0) {
                        log.debug("好友申请条数:{}", friendApplications.size());
                        sendMessage(channel, JsonResult.success(friendApplications, Cmd.ADD_FRIEND));
                        List<String> ids = new ArrayList<>();
                        friendApplications.forEach(friendApplication -> ids.add(friendApplication.getId()));
                        friendApplicationService.updateFriendApplicationRead(true, ids);
                        log.debug("修改好友申请消息为已读 {}ms", DateUtil.spendMs(begin));
                    }
                    List<SingleChat> singleChats = singleChatService.listByReadAndToUserId(false, userId);
                    if (singleChats.size() > 0) {
                        log.debug("单聊条数:{}", friendApplications.size());
                        singleChats.forEach(singleChat -> sendMessage(channel, JsonResult.success(singleChat, Cmd.SINGLE_CHAT)));
                        singleChatService.updateRead(true, singleChats);
                        log.debug("修改单聊消息为已读 {}ms", DateUtil.spendMs(begin));
                    }
                    user.setLoginState(UserConstant.LOGGED);
                    userService.updateRedisDataByUid(user, "RegisterCmd 注册用户Channel");
                    log.debug("用户注册完毕 {}ms", DateUtil.spendMs(begin));
                    log.debug(LogUtil.INTERVAL);
                    return;
                }
                log.debug("用户重复注册");
                throw new UserAlreadyRegister();
            }
            log.error("token验证失败!");
            throw new UnAuthorizedException();
        } catch (UsernameNotFoundException e) {
            e.printStackTrace();
            log.error("token解析失败!");
            remove(channel);
        }
    }

    /**
     * 检查用户是否在线
     *
     * @param userId  用户ID
     * @param channel 用户channel
     * @return boolean
     */
    private boolean checkUserOnline(Integer userId, Channel channel) {
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
