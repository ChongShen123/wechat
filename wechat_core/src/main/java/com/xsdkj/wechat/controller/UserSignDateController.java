package com.xsdkj.wechat.controller;

import cn.hutool.core.date.DateUtil;
import com.xsdkj.wechat.common.JsonResult;
import com.xsdkj.wechat.dto.GiveRetroactiveCountDto;
import com.xsdkj.wechat.dto.GiveScoreDto;
import com.xsdkj.wechat.dto.RetroactiveDto;
import com.xsdkj.wechat.service.UserSignDateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 签到控制器
 *
 * @author tiankong
 * @date 2020/1/5 11:23
 */
@Slf4j
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
        long currentTimeMillis = System.currentTimeMillis();
        userSignDateService.singDate();
        JsonResult success = JsonResult.success();
        log.debug(DateUtil.spendMs(currentTimeMillis) + "");
        return success;
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

    /**
     * 管理员: 赠送用户积分
     *
     * @param giveScoreDto 参数
     * @return JsonResult
     */
    @PostMapping("/give_score")
    public JsonResult adminGiveScore(@Validated @RequestBody GiveScoreDto giveScoreDto) {
        long begin = System.currentTimeMillis();
        userSignDateService.giveScore(giveScoreDto);
        JsonResult success = JsonResult.success();
        log.debug(DateUtil.spendMs(begin) + "");
        return success;
    }

    /**
     * 用户补签
     *
     * @return JsonResult
     */
    @PostMapping("/retroactive")
    public JsonResult retroactive(@Validated @RequestBody RetroactiveDto retroactiveDto) {
        long begin = System.currentTimeMillis();
        userSignDateService.retroactive(retroactiveDto);
        JsonResult success = JsonResult.success();
        log.debug(DateUtil.spendMs(begin) + "");
        return success;
    }
}
