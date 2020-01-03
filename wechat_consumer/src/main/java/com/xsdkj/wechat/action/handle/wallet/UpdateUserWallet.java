package com.xsdkj.wechat.action.handle.wallet;

import com.xsdkj.wechat.action.MsgHandler;
import com.xsdkj.wechat.action.SaveAnno;
import com.xsdkj.wechat.bo.MsgBox;
import com.xsdkj.wechat.constant.RabbitConstant;
import com.xsdkj.wechat.entity.wallet.Wallet;
import com.xsdkj.wechat.service.UserWalletService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author tiankong
 * @date 2020/1/3 12:33
 */
@Component
@SaveAnno(type = RabbitConstant.BOX_TYPE_UPDATE_USER_WALLET)
@Slf4j
public class UpdateUserWallet implements MsgHandler {
    @Resource
    private UserWalletService userWalletService;

    @Override
    public void execute(MsgBox box) {
        Wallet wallet = (Wallet) box.getData();
        log.info("用户钱包操作记录>>>{}", wallet);
        userWalletService.updateUserWallet(wallet);
    }
}
