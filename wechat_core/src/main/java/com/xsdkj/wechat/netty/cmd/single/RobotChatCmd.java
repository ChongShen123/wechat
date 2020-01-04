package com.xsdkj.wechat.netty.cmd.single;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.xsdkj.wechat.constant.SystemConstant;
import com.xsdkj.wechat.constant.ParamConstant;
import com.xsdkj.wechat.netty.cmd.base.AbstractChatCmd;
import com.xsdkj.wechat.ex.ValidateException;
import io.netty.channel.Channel;

/**
 * 机器人要付费的.暂时不要用了.
 *
 * @author tiankong
 * @date 2019/12/26 17:38
 */
//@CmdAnno(cmd = Cmd.ROBOT_CHAT)
//@Service
@Deprecated
public class RobotChatCmd extends AbstractChatCmd {
    @Override
    protected void parseParam(JSONObject param) throws Exception {
        String content = param.getString(ParamConstant.KEY_CONTENT);
        if (StrUtil.isBlank(content)) {
            throw new ValidateException();
        }
        requestParam.setContent(content);
    }

    @Override
    protected void concreteAction(Channel channel) {
        String content = requestParam.getContent();
        String response = HttpUtil.get(SystemConstant.ROBOT_URL + content);
    }
}
