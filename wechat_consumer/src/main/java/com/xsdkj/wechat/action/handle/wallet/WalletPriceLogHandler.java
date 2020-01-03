package com.xsdkj.wechat.action.handle.wallet;

import com.xsdkj.wechat.action.MsgHandler;
import com.xsdkj.wechat.action.SaveAnno;
import com.xsdkj.wechat.bo.MsgBox;
import com.xsdkj.wechat.constant.RabbitConstant;
import com.xsdkj.wechat.entity.wallet.WalletPriceLog;
import com.xsdkj.wechat.service.UserWalletService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author tiankong
 * @date 2020/1/3 12:58
 */
@Component
@SaveAnno(type = RabbitConstant.BOX_TYPE_USER_PRICE_OPERATION_LOG)
@Slf4j
public class WalletPriceLogHandler implements MsgHandler {
    @Resource
    private UserWalletService userWalletService;

    @Override
    public void execute(MsgBox box) {
        WalletPriceLog walletPriceLog = (WalletPriceLog) box.getData();
        log.info("用户金额操作流水记录>>>{}", walletPriceLog);
        userWalletService.saveWalletPriceLog(walletPriceLog);
    }
}
