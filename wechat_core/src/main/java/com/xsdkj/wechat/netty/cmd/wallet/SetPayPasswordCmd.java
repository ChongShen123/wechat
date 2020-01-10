package com.xsdkj.wechat.netty.cmd.wallet;

import cn.hutool.core.date.DateUtil;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 说明:只有用户第一次可以设置支付密码.如果支付密码不为空则不允许用户设置.
 *
 * @author tiankong
 * @date 2020/1/4 16:14
 */

@Slf4j
@CmdAnno(cmd = Cmd.SET_PAY_PASSWORD)
@Component
public class SetPayPasswordCmd extends AbstractChatCmd {
    @Resource
    private UserWalletService userWalletService;

    @Override
    protected void parseParam(JSONObject param) {
        requestParam.setPassword(parseParam(param, ParamConstant.KEY_PASSWORD));
    }

    @Override
    protected void concreteAction(Channel channel) throws RuntimeException {
        log.debug("开始处理设置支付密码...");
        long begin = System.currentTimeMillis();
        String password = requestParam.getPassword();
        Wallet wallet = userWalletService.getByUid(session.getUid(), true);
        Byte state = wallet.getState();
        if (state != null) {
            log.error("支付密码已被设置");
            throw new PayPasswordHasBeenSetException();
        }
        if (!password.matches(SystemConstant.PASSWORD_REGEX)) {
            log.error("密码格式不正确");
            throw new ValidateException();
        }
        userWalletService.updatePayPassword(session.getUid(), password);
        sendMessage(channel, JsonResult.success());
        userService.updateRedisDataByUid(session.getUid(), "SetPayPasswordCmd.concreteAction()");
        log.debug("设置支付密码完成 {}ms", DateUtil.spendMs(begin));
    }
}
