package com.xsdkj.wechat.controller;

import com.xsdkj.wechat.common.JsonResult;
import com.xsdkj.wechat.dto.UserPriceOperationDto;
import com.xsdkj.wechat.service.UserWalletService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author tiankong
 * @date 2020/1/3 10:29
 */
@RestController
@RequestMapping("/wallet")
public class UserWalletController {
    @Resource
    private UserWalletService userWalletService;

    @PostMapping("/priceOperation")
    public JsonResult priceOperation(@Validated @RequestBody UserPriceOperationDto userPriceOperationDto) {
        userWalletService.priceOperation(userPriceOperationDto);
        return JsonResult.success();
    }
}
