package com.xsdkj.wechat.netty.cmd.wallet;

import com.alibaba.fastjson.JSONObject;
import com.xsdkj.wechat.common.Cmd;
import com.xsdkj.wechat.common.JsonResult;
import com.xsdkj.wechat.constant.ParamConstant;
import com.xsdkj.wechat.constant.SystemConstant;
import com.xsdkj.wechat.entity.wallet.Wallet;
import com.xsdkj.wechat.netty.cmd.CmdAnno;
import com.xsdkj.wechat.netty.cmd.base.AbstractChatCmd;
import com.xsdkj.wechat.service.UserWalletService;
import com.xsdkj.wechat.ex.PayPasswordHasBeenSetException;
import com.xsdkj.wechat.ex.ValidateException;
import io.netty.channel.Channel;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 说明:只有用户第一次可以设置支付密码.如果支付密码不为空则不允许用户设置.
 *
 * @author tiankong
 * @date 2020/1/4 16:14
 */

@Component
@CmdAnno(cmd = Cmd.SET_PAY_PASSWORD)
public class SetPayPasswordCmd extends AbstractChatCmd {
    @Resource
    private UserWalletService userWalletService;

    @Override
    protected void parseParam(JSONObject param) {
        requestParam.setPassword(parseParam(param, ParamConstant.KEY_PASSWORD));
    }

    @Override
    protected void concreteAction(Channel channel) throws RuntimeException {
        String password = requestParam.getPassword();
        Wallet wallet = userWalletService.getByUid(session.getUid(), true);
        Byte state = wallet.getState();
        if (state != null) {
            throw new PayPasswordHasBeenSetException();
        }
        if (!password.matches(SystemConstant.PASSWORD_REGEX)) {
            throw new ValidateException();
        }
        userWalletService.updatePayPassword(session.getUid(), password);
        sendMessage(channel, JsonResult.success());
        userService.updateRedisDataByUid(session.getUid());
    }
}
