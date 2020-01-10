package com.xsdkj.wechat.netty.cmd.group;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.xsdkj.wechat.common.Cmd;
import com.xsdkj.wechat.common.JsonResult;
import com.xsdkj.wechat.constant.ParamConstant;
import com.xsdkj.wechat.netty.cmd.CmdAnno;
import com.xsdkj.wechat.netty.cmd.base.AbstractChatCmd;
import com.xsdkj.wechat.ex.FileNotFoundException;
import com.xsdkj.wechat.ex.PermissionDeniedException;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 修改群信息
 * 可以修改群名称,头像,公告
 *
 * @author tiankong
 * @date 2019/12/30 13:35
 */
@Slf4j
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
        String path = param.getString(ParamConstant.KEY_ICON);
        if (path != null) {
            requestParam.setIcon(path);
        }
        requestParam.setNotice(parseParam(param, ParamConstant.KEY_NOTICE));
    }

    @Override
    protected void concreteAction(Channel channel) throws RuntimeException {
        long begin = System.currentTimeMillis();
        try {
            log.debug("开始处理修改群相关业务...");
            Integer groupId = requestParam.getGroupId();
            String icon = requestParam.getIcon();
            String notice = requestParam.getNotice();
            String name = requestParam.getName();
            if (!checkAdmin(groupId, session.getUid())) {
                log.error("用户{}没有相关权限", session.getUid());
                throw new PermissionDeniedException();
            }
            if (StrUtil.isNotBlank(icon)) {
                String path = root + imgPath + icon;
                if (!FileUtil.exist(path)) {
                    log.error("文件{}不存在", path);
                    throw new FileNotFoundException();
                }
            }
            groupService.updateGroupInfo(groupId, name, icon, notice);
            groupService.updateRedisGroupById(groupId);
            sendMessage(channel, JsonResult.success(cmd));
        } finally {
            log.debug("业务处理完成 {}ms", DateUtil.spendMs(begin));
        }
    }
}
