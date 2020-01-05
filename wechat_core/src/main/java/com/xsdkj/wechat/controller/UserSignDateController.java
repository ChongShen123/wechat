package com.xsdkj.wechat.controller;

import com.xsdkj.wechat.common.JsonResult;
import com.xsdkj.wechat.service.UserSignDateService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
