package com.xsdkj.wechat.netty.cmd.group;

import cn.hutool.core.io.FileUtil;
import com.alibaba.fastjson.JSONObject;
import com.xsdkj.wechat.common.Cmd;
import com.xsdkj.wechat.common.JsonResult;
import com.xsdkj.wechat.constant.ParamConstant;
import com.xsdkj.wechat.netty.cmd.CmdAnno;
import com.xsdkj.wechat.netty.cmd.base.AbstractChatCmd;
import com.xsdkj.wechat.service.ex.FileNotFoundException;
import com.xsdkj.wechat.service.ex.PermissionDeniedException;
import io.netty.channel.Channel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 修改群信息
 * 可以修改群名称,头像,公告
 *
 * @author tiankong
 * @date 2019/12/30 13:35
 */
@Component
@CmdAnno(cmd = Cmd.UPDATE_GROUP_INFO)
public class UpdateGroupInfo extends AbstractChatCmd {
    @Value("${file.root-path}")
    private String root;
    @Value(("${file.img-path}"))
    private String imgPath;

    @Override
    protected void parseParam(JSONObject param) {
        requestParam.setGroupId(Integer.parseInt(parseParam(param, ParamConstant.KEY_GROUP_ID)));
        requestParam.setName(parseParam(param, ParamConstant.KEY_GROUP_NAME));
        requestParam.setIcon(parseParam(param, ParamConstant.KEY_ICON));
        requestParam.setNotice(parseParam(param, ParamConstant.KEY_NOTICE));
    }

    @Override
    protected void concreteAction(Channel channel) throws RuntimeException {
        Integer groupId = requestParam.getGroupId();
        String icon = requestParam.getIcon();
        String notice = requestParam.getNotice();
        String name = requestParam.getName();
        if (!checkAdmin(groupId, session.getUid())) {
            throw new PermissionDeniedException();
        }
        if (!FileUtil.exist(root + imgPath + icon)) {
            throw new FileNotFoundException();
        }
        groupService.updateGroupInfo(groupId, name, icon, notice);
        groupService.updateRedisGroupById(groupId);
        sendMessage(channel, JsonResult.success(cmd));
    }
}
