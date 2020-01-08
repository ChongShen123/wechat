package com.xsdkj.wechat.controller;

import com.xsdkj.wechat.common.JsonResult;
import com.xsdkj.wechat.dto.GiveRetroactiveCountDto;
import com.xsdkj.wechat.dto.GiveScoreDto;
import com.xsdkj.wechat.service.UserSignDateService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 签到控制器
 *
 * @author tiankong
 * @date 2020/1/5 11:23
 */
@RestController
@RequestMapping("/sign_date")
public class UserSignDateController {
    @Resource
    private UserSignDateService userSignDateService;

    /**
     * 用户签到
     *
     * @return JsonResult
     */
    @GetMapping
    public JsonResult singDate() {
        userSignDateService.singDate();
        return JsonResult.success();
    }

    /**
     * 管理员: 赠送用户补签次数
     *
     * @param giveRetroactiveCountDto 参数
     * @return JsonResult
     */
    @PostMapping("/give_retroactive_count")
    public JsonResult adminGiveRetroactiveCount(@Validated @RequestBody GiveRetroactiveCountDto giveRetroactiveCountDto) {
        userSignDateService.giveRetroactiveCount(giveRetroactiveCountDto);
        return JsonResult.success();
    }

    @PostMapping("/give_score")
    public JsonResult adminGiveScore(@Validated @RequestBody GiveScoreDto giveScoreDto) {
        userSignDateService.giveScore(giveScoreDto);
        return JsonResult.success();
    }
}
