package com.xsdkj.wechat.action.handle.wallet;

import com.xsdkj.wechat.action.MsgHandler;
import com.xsdkj.wechat.action.SaveAnno;
import com.xsdkj.wechat.bo.MsgBox;
import com.xsdkj.wechat.constant.RabbitConstant;
import com.xsdkj.wechat.entity.wallet.WalletOperationLog;
import com.xsdkj.wechat.service.UserWalletService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author tiankong
 * @date 2020/1/3 12:58
 */
@Slf4j
@Component
@SaveAnno(type = RabbitConstant.BOX_TYPE_USER_PRICE_OPERATION_LOG)
public class WalletPriceOperationLogHandler implements MsgHandler {
    @Resource
    private UserWalletService userWalletService;

    @Override
    public void execute(MsgBox box) {
        WalletOperationLog walletOperationLog = (WalletOperationLog) box.getData();
        log.info("用户金额操作流水记录>>>{}", walletOperationLog);
        userWalletService.saveWalletOperationLog(walletOperationLog);
    }
}
