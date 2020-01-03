package com.xsdkj.wechat.controller;

import com.xsdkj.wechat.common.JsonResult;
import com.xsdkj.wechat.dto.MoodParamDto;
import com.xsdkj.wechat.entity.mood.UserMood;
import com.xsdkj.wechat.service.UserMoodService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author Administrator
 */
@RestController
@RequestMapping("/mood")
public class UserMoodController {
    @Resource
    private UserMoodService userMoodService;
    @PostMapping("/save")
    public JsonResult userSaveMood(@Validated @RequestBody MoodParamDto moodDto){
        userMoodService.save(moodDto);
        return JsonResult.success();
    }
    @PostMapping("/delete")
    public  void deleteMood(@RequestBody UserMood userMood){
        userMoodService.delete(userMood);
    }

}
