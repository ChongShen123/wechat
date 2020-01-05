package com.xsdkj.wechat.controller;

import com.xsdkj.wechat.common.JsonResult;
import com.xsdkj.wechat.dto.GiveRetroactiveCountDto;
import com.xsdkj.wechat.service.UserSignDateService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 签到控制类
 *
 * @author tiankong
 * @date 2020/1/5 11:23
 */
@RestController
@RequestMapping("/sign_date")
public class UserSignDateController {
    @Resource
    private UserSignDateService userSignDateService;

    @GetMapping
    public JsonResult singDate() {
        userSignDateService.singDate();
        return JsonResult.success();
    }

    @PostMapping("/give_retroactive_count")
    public JsonResult giveRetroactiveCount(@Validated @RequestBody GiveRetroactiveCountDto giveRetroactiveCountDto) {
        userSignDateService.giveRetroactiveCount(giveRetroactiveCountDto);
        return JsonResult.success();
    }
}
