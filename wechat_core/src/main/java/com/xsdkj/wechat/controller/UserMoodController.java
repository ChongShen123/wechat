package com.xsdkj.wechat.controller;

import com.xsdkj.wechat.common.JsonResult;
import com.xsdkj.wechat.dto.MoodParamDto;
import com.xsdkj.wechat.dto.UserThumbsDto;
import com.xsdkj.wechat.entity.chat.UserComment;
import com.xsdkj.wechat.entity.chat.UserMood;
import com.xsdkj.wechat.entity.chat.UserThumbs;
import com.xsdkj.wechat.service.UserMoodService;
import com.xsdkj.wechat.vo.UserMoodVo;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

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

    /**
     * 查询所有好友的动态，点赞，评论
     * @return
     */
    @PostMapping("/selectAllMood")
    public JsonResult selectAllMood(){
        userMoodService.selectAll();
        List<UserMoodVo> userMoodVos = userMoodService.listUserMoodByUid();
        return  JsonResult.success(userMoodVos);

    }

}
