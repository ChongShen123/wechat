package com.xsdkj.wechat.controller;

import cn.hutool.core.date.DateUtil;
import com.xsdkj.wechat.common.JsonPage;
import com.xsdkj.wechat.common.JsonResult;
import com.xsdkj.wechat.dto.GiveScoreDto;
import com.xsdkj.wechat.dto.ListUserScoreDto;
import com.xsdkj.wechat.service.UserScoreService;
import com.xsdkj.wechat.vo.UserScoreVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 用户积分控制器
 *
 * @author tiankong
 * @date 2020/1/6 15:31
 */
@Slf4j
@RestController
@RequestMapping("/userScore")
public class UserScoreController {
    @Resource
    private UserScoreService userScoreService;

    /**
     * 查询用户积分
     *
     * @return JsonResult
     */
    @GetMapping()
    public JsonResult getUserScore() {
        UserScoreVo scoreVo = userScoreService.getUserScore();
        return JsonResult.success(scoreVo);
    }

    /**
     * 查询用户积分列表
     *
     * @param listUserScoreDto 参数
     * @return JsonResult
     */
    @PostMapping("/listUserScore")
    public JsonResult listUserScore(@RequestBody ListUserScoreDto listUserScoreDto) {
        List<UserScoreVo> list = userScoreService.listUserScore(listUserScoreDto);
        return JsonResult.success(JsonPage.restPage(list));
    }

    /**
     * 管理员: 赠送用户积分 批量
     *
     * @param giveScoreDto 参数
     * @return JsonResult
     */
    @PostMapping("/giveScore")
    public JsonResult adminGiveScore(@Validated @RequestBody GiveScoreDto giveScoreDto) {
        long begin = System.currentTimeMillis();
        userScoreService.handleGiveScore(giveScoreDto);
        JsonResult success = JsonResult.success();
        log.debug(DateUtil.spendMs(begin) + "");
        return success;
    }

    /**
     * 管理员赠送积分 全体
     */
    @GetMapping("/giveScoreAll/{score}")
    public JsonResult adminGiveScoreAll(@PathVariable(name = "score") Integer score) {
        long begin = System.currentTimeMillis();
        userScoreService.handleGiveScoreAll(score);
        log.debug("赠送积分全体 完成 {}ms", DateUtil.spendMs(begin));
        return JsonResult.success();
    }
}
