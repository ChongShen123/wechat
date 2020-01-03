package com.xsdkj.wechat.action.handle.wallet;

import com.xsdkj.wechat.action.MsgHandler;
import com.xsdkj.wechat.action.SaveAnno;
import com.xsdkj.wechat.bo.MsgBox;
import com.xsdkj.wechat.constant.RabbitConstant;
import com.xsdkj.wechat.entity.wallet.WalletPriceChangeLog;
import com.xsdkj.wechat.service.UserWalletService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author tiankong
 * @date 2020/1/3 17:36
 */

@Component
@SaveAnno(type = RabbitConstant.BOX_TYPE_USER_PRICE_CHANGE_LOG)
@Slf4j
public class WalletPriceChangeLogHandler implements MsgHandler {
    @Resource
    private UserWalletService userWalletService;

    @Override
    public void execute(MsgBox box) {
        log.info("用户账变记录>>>{}", box.getData());
        WalletPriceChangeLog walletPriceChangeLog = (WalletPriceChangeLog) box.getData();
        userWalletService.saveWalletPriceChangeLog(walletPriceChangeLog);
    }
}
