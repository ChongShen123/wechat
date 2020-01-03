package com.xsdkj.wechat.controller;

import com.xsdkj.wechat.common.JsonResult;
import com.xsdkj.wechat.dto.UserCommentDto;
import com.xsdkj.wechat.entity.mood.UserComment;
import com.xsdkj.wechat.service.UserCommentService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author Administrator
 */
@RestController
@RequestMapping("/comment")
public class UserCommentController {
    @Resource
    UserCommentService userCommentService;
    @RequestMapping("/save")
    private JsonResult commentSave(@Validated @RequestBody UserCommentDto userCommentDto){
        userCommentService.save(userCommentDto);
        return JsonResult.success();
    }
    @RequestMapping("/delete")
    private void commentDelete(@RequestBody UserComment userComment){
        userCommentService.delete(userComment);
    }
}
