package com.xsdkj.wechat.controller;

import com.xsdkj.wechat.dto.UserThumbsDto;
import com.xsdkj.wechat.entity.mood.UserThumbs;
import com.xsdkj.wechat.service.UserThumbsService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author Administrator
 */
@RestController
@RequestMapping("/thumbs")
public class UserThumbsController {

    @Resource
    UserThumbsService userThumbsService;
    @RequestMapping("/save")
    public void thumbsSave(@RequestBody UserThumbsDto userThumbsDto){
        userThumbsService.save(userThumbsDto);
    }

    @RequestMapping("/delete")
    public void thumbsDelete(@RequestBody UserThumbs userThumbs){
        userThumbsService.delete(userThumbs);

    }
}
