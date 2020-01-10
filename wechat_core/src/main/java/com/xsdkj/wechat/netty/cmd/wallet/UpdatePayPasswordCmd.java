package com.xsdkj.wechat.netty.cmd.wallet;

import cn.hutool.core.date.DateUtil;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 修改用户支付密码
 *
 * @author tiankong
 * @date 2020/1/4 16:14
 */
@Slf4j
@CmdAnno(cmd = Cmd.UPDATE_PAY_PASSWORD)
@Component
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
        long begin = System.currentTimeMillis();
        log.debug("开始处理修改支付密码业务...");
        String password = requestParam.getPassword();
        String oldPassword = requestParam.getOldPassword();
        Wallet wallet = userWalletService.getByUid(session.getUid(), true);
        if (wallet == null) {
            log.error("用户{}钱包为空", session.getUid());
            throw new DataEmptyException();
        }
        userWalletService.resetPayPassword(session.getUid(), password, oldPassword);
        sendMessage(channel, JsonResult.success(cmd));
        userService.updateRedisDataByUid(session.getUid(), "UpdatePayPasswordCmd.concreteAction() 这里是否可以不用更新?");
        log.debug("业务处理完成 {}ms", DateUtil.spendMs(begin));
    }
}
