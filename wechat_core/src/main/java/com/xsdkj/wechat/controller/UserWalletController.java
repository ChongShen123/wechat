package com.xsdkj.wechat.controller;

import cn.hutool.core.date.DateUtil;
import com.xsdkj.wechat.common.JsonResult;
import com.xsdkj.wechat.dto.UserPriceOperationDto;
import com.xsdkj.wechat.service.UserWalletService;
import com.xsdkj.wechat.util.LogUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.concurrent.TimeoutException;

/**
 * @author tiankong
 * @date 2020/1/3 10:29
 */
@Slf4j
@RestController
@RequestMapping("/wallet")
public class UserWalletController {
    @Resource
    private UserWalletService userWalletService;

    /**
     * 用户充值或提现
     *
     * @param userPriceOperationDto 参数
     * @return json
     */
    @PostMapping("/priceOperation")
    public JsonResult priceOperation(@Validated @RequestBody UserPriceOperationDto userPriceOperationDto) {
        log.debug(LogUtil.INTERVAL);
        long begin = System.currentTimeMillis();
        log.debug("开始处理充值提现业务...");
        userWalletService.priceOperation(userPriceOperationDto);
        JsonResult jsonResult = JsonResult.success();
        log.debug("业务处理完成 {}ms", DateUtil.spendMs(begin));
        log.debug(LogUtil.INTERVAL);
        return jsonResult;
    }
}
