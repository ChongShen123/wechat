package com.xsdkj.wechat.netty.cmd.friend;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.xsdkj.wechat.common.Cmd;
import com.xsdkj.wechat.common.JsonResult;
import com.xsdkj.wechat.common.SystemConstant;
import com.xsdkj.wechat.service.ex.UserNotFountException;
import com.xsdkj.wechat.netty.cmd.CmdAnno;
import com.xsdkj.wechat.netty.cmd.base.BaseChatCmd;
import com.xsdkj.wechat.vo.ListUserFriendVo;
import io.netty.channel.Channel;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author tiankong
 * @date 2019/12/25 18:36
 */
@CmdAnno(cmd = Cmd.DELETE_FRIEND)
@Service
public class DeleteFriendCmd extends BaseChatCmd {
    @Override
    protected void parseParam(JSONObject param) throws Exception {
        Integer fid = param.getInteger(SystemConstant.KEY_FRIEND_ID);
        if (ObjectUtil.isEmpty(fid)) {
            throw new UserNotFountException();
        }
        requestParam.setFriendId(fid);
    }

    @Override
    protected void concreteAction(Channel channel) {
        Integer friendId = requestParam.getFriendId();
        if (!checkIsFriend(session.getUid(), friendId)) {
            throw new UserNotFountException();
        }
        userService.deleteFriend(session.getUid(), friendId);
        sendMessage(channel, JsonResult.success(cmd));
    }

    /**
     * 检查是否为朋友
     *
     * @param uid      用户ID
     * @param friendId 好友ID
     * @return boolean
     */
    private boolean checkIsFriend(Integer uid, Integer friendId) {
        List<ListUserFriendVo> listUserFriendVos = userService.listFriendByUserId(uid);
        for (ListUserFriendVo friendVo : listUserFriendVos) {
            if (friendVo.getUid().equals(friendId)) {
                return true;
            }
        }
        return false;
    }
}
