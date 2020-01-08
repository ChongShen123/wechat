package com.xsdkj.wechat.netty.cmd.group;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.xsdkj.wechat.common.Cmd;
import com.xsdkj.wechat.common.JsonResult;
import com.xsdkj.wechat.constant.GroupConstant;
import com.xsdkj.wechat.entity.user.UserGroup;
import com.xsdkj.wechat.ex.*;
import com.xsdkj.wechat.netty.cmd.CmdAnno;
import com.xsdkj.wechat.netty.cmd.base.AbstractChatCmd;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 设置群管理员
 *
 * @author tiankong
 * @date 2019/12/28 17:46
 */
@Slf4j
@CmdAnno(cmd = Cmd.SET_GROUP_MANAGER)
@Service
public class SetGroupManagerCmd extends AbstractChatCmd {
    @Override
    protected void parseParam(JSONObject param) {
        parseGroupId(param);
        parseUserId(param);
        parseIntegerType(param);
    }

    @Override
    protected void concreteAction(Channel channel) throws RuntimeException {
        long begin = System.currentTimeMillis();
        log.debug("开始处理群管理员增删操作...");
        // 群id
        try {
            Integer groupId = requestParam.getGroupId();
            // 管理员id
            Integer userId = requestParam.getUserId();
            UserGroup group = groupService.getGroupById(groupId);
            if (group != null) {
                // 只有群主可以管理员
                if (group.getOwnerId().equals(session.getUid())) {
                    Integer type = requestParam.getIntType();
                    switch (type) {
                        // 添加管理员
                        case GroupConstant.ADD_MANAGER:
                            Integer count = groupService.countGroupManger(group.getId(), userId);
                            if (count == 0) {
                                groupService.addGroupManager(group.getId(), userId);
                                log.debug("用户{}已被设置为{}群管理员", userId, group.getId());
                                break;
                            }
                            throw new RepetitionException();
                            // 删除管理员
                        case GroupConstant.DELETE_MANAGER:
                            count = groupService.countGroupManger(group.getId(), userId);
                            if (count == 1) {
                                groupService.deleteGroupManager(group.getId(), userId);
                                log.debug("用户{}已在{}群管理员已被取消", userId, group.getId());
                                break;
                            } else if (count > 1) {
                                log.error("查询出了多少数据 {}", count);
                                throw new ServiceException();
                            }
                            throw new DataEmptyException();
                        default:
                            throw new ValidateException();
                    }
                    sendMessage(channel, JsonResult.success(cmd));
                    return;
                }
                log.error("只有群主{}拥有此权限", group.getOwnerId());
                throw new PermissionDeniedException();
            }
            log.error("{}群不存在", groupId);
            throw new DataEmptyException();
        } finally {
            log.debug("本次业务操作完成用时 {}ms", DateUtil.spendMs(begin));
        }
    }
}
