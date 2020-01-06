package com.xsdkj.wechat.controller;

import com.xsdkj.wechat.common.JsonResult;
import com.xsdkj.wechat.service.UserScoreService;
import com.xsdkj.wechat.vo.UserScoreVo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 用户积分控制器
 *
 * @author tiankong
 * @date 2020/1/6 15:31
 */
@RestController
@RequestMapping("/user_score")
public class UserScoreController {
    @Resource
    private UserScoreService userScoreService;

    /**
     * 查询用户积分
     * @return JsonResult
     */
    @GetMapping()
    public JsonResult getUserScore() {
        UserScoreVo scoreVo = userScoreService.getUserScore();
        return JsonResult.success(scoreVo);
    }
}
