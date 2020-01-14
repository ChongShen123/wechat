package com.xsdkj.wechat.controller;

import cn.hutool.core.date.DateUtil;
import com.xsdkj.wechat.common.JsonPage;
import com.xsdkj.wechat.common.JsonResult;
import com.xsdkj.wechat.dto.GiveRetroactiveCountDto;
import com.xsdkj.wechat.dto.RetroactiveDto;
import com.xsdkj.wechat.dto.UserSignDateDetailDto;
import com.xsdkj.wechat.dto.UserSignDateDto;
import com.xsdkj.wechat.service.UserSignDateService;
import com.xsdkj.wechat.vo.UserSignDateDetailVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 签到控制器
 *
 * @author tiankong
 * @date 2020/1/5 11:23
 */
@Slf4j
@RestController
@RequestMapping("/signDate")
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
        userSignDateService.handleSignDate();
        JsonResult success = JsonResult.success();
        log.debug(DateUtil.spendMs(currentTimeMillis) + "");
        return success;
    }

    /**
     * 管理员: 赠送用户补签次数 批量
     *
     * @param giveRetroactiveCountDto 参数
     * @return JsonResult
     */
    @PostMapping("/giveRetroactiveCount")
    public JsonResult adminGiveRetroactiveCount(@Validated @RequestBody GiveRetroactiveCountDto giveRetroactiveCountDto) {
        long begin = System.currentTimeMillis();
        userSignDateService.handleGiveRetroactiveCount(giveRetroactiveCountDto);
        JsonResult success = JsonResult.success();
        log.debug("修改用户补签次数完成 {}ms", DateUtil.spendMs(begin));
        return success;
    }

    /**
     * 管理员: 赠送用户补签次数 全体
     *
     * @param giveRetroactiveAllCount 参数
     * @return JsonResult
     */
    @GetMapping("/giveRetroactiveCount/{count}")
    public JsonResult adminGiveRetroactiveCountAll(@PathVariable(name = "count") Integer giveRetroactiveAllCount) {
        long begin = System.currentTimeMillis();
        userSignDateService.handleGiveRetroactiveCountAll(giveRetroactiveAllCount);
        JsonResult success = JsonResult.success();
        log.debug("修改用户补签次数完成 {}ms", DateUtil.spendMs(begin));
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
        userSignDateService.handleRetroactive(retroactiveDto);
        JsonResult success = JsonResult.success();
        log.debug(DateUtil.spendMs(begin) + "");
        return success;
    }

    /**
     * 查询用户签到情况
     *
     * @return JsonResult
     */
    @PostMapping("/listUserSignDate")
    public JsonResult userSignDate(@Validated @RequestBody UserSignDateDto userSignDateDto) {
        return JsonResult.success(JsonPage.restPage(userSignDateService.listSignDate(userSignDateDto)));
    }

    @PostMapping("/listUserSignDateDetail")
    public JsonResult listUserSignDateDetail(@Validated @RequestBody UserSignDateDetailDto userSignDateDetailDto) {
        List<UserSignDateDetailVo> list = userSignDateService.listUserSignDateDetail(userSignDateDetailDto);
        return JsonResult.success(JsonPage.restPage(list));
    }

}
