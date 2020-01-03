package com.xsdkj.wechat.netty.cmd.wallet;

import com.alibaba.fastjson.JSONObject;
import com.xsdkj.wechat.common.Cmd;
import com.xsdkj.wechat.constant.ParamConstant;
import com.xsdkj.wechat.netty.cmd.CmdAnno;
import com.xsdkj.wechat.netty.cmd.base.AbstractChatCmd;
import io.netty.channel.Channel;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * @author tiankong
 * @date 2020/1/3 15:27
 */
@Component
@CmdAnno(cmd = Cmd.TRANSFER_ACCOUNTS)
public class TransferUserPriceCmd extends AbstractChatCmd {
    @Override
    protected void parseParam(JSONObject param) {
        requestParam.setToUserId(Integer.parseInt(parseParam(param, ParamConstant.KEY_TO_USER_ID)));
        requestParam.setPrice(new BigDecimal(parseParam(param, ParamConstant.KEY_PRICE)));
    }

    @Override
    protected void concreteAction(Channel channel) throws RuntimeException {
        Integer toUserId = requestParam.getToUserId();
        BigDecimal price = requestParam.getPrice();
        // 判断用户余额是否足够
        // 判断对方是否存在
        // 创建一条转账记录
        //
    }
}
