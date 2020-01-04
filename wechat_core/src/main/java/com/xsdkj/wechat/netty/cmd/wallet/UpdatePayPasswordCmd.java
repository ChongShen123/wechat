package com.xsdkj.wechat.netty.cmd.wallet;

import com.alibaba.fastjson.JSONObject;
import com.xsdkj.wechat.common.Cmd;
import com.xsdkj.wechat.common.JsonResult;
import com.xsdkj.wechat.constant.ParamConstant;
import com.xsdkj.wechat.constant.SystemConstant;
import com.xsdkj.wechat.entity.wallet.Wallet;
import com.xsdkj.wechat.ex.DataEmptyException;
import com.xsdkj.wechat.ex.ValidateException;
import com.xsdkj.wechat.netty.cmd.CmdAnno;
import com.xsdkj.wechat.netty.cmd.base.AbstractChatCmd;
import com.xsdkj.wechat.service.UserWalletService;
import io.netty.channel.Channel;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author tiankong
 * @date 2020/1/4 16:14
 */
@Component
@CmdAnno(cmd = Cmd.UPDATE_PAY_PASSWORD)
public class UpdatePayPasswordCmd extends AbstractChatCmd {
    @Resource
    private UserWalletService userWalletService;

    @Override
    protected void parseParam(JSONObject param) {
        String password = parseParam(param, ParamConstant.KEY_PASSWORD);
        String oldPassword = parseParam(param, ParamConstant.KEY_OLD_PASSWORD);
        if (!password.matches(SystemConstant.PASSWORD_REGEX) || !oldPassword.matches(SystemConstant.PASSWORD_REGEX)) {
            throw new ValidateException();
        }
        requestParam.setPassword(password);
        requestParam.setOldPassword(oldPassword);
    }

    @Override
    protected void concreteAction(Channel channel) throws RuntimeException {
        String password = requestParam.getPassword();
        String oldPassword = requestParam.getOldPassword();
        Wallet wallet = userWalletService.getByUid(session.getUid(), true);
        if (wallet == null) {
            throw new DataEmptyException();
        }
        userWalletService.resetPayPassword(session.getUid(), password, oldPassword);
        sendMessage(channel, JsonResult.success(cmd));
        userService.updateRedisDataByUid(session.getUid());
    }
}
