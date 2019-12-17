package com.cxkj.wechat.controller;

import com.cxkj.wechat.dto.UserLoginParam;
import com.cxkj.wechat.dto.UserRegisterParam;
import com.cxkj.wechat.dto.UserUpdateInfoParam;
import com.cxkj.wechat.entity.User;
import com.cxkj.wechat.service.UserService;
import com.cxkj.wechat.util.JsonResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author tiankong
 * @date 2019/12/11 14:55
 */
@RestController
@RequestMapping("/user")
public class UserController extends BaseController {
    @Resource
    private UserService userService;

    @PostMapping("/register")
    public JsonResult register(@Validated @RequestBody UserRegisterParam param, HttpServletRequest request) {
        return JsonResult.success(userService.register(param, request));
    }

    @PostMapping("/login")
    public JsonResult login(@Validated @RequestBody UserLoginParam param, HttpServletRequest request) {
        return JsonResult.success(userService.login(param, request, true));
    }
/*    @PostMapping("updateuserinfo")
    public JsonResult updateuserinfo(@Validated @RequestBody UserUpdateInfoParam param ,HttpServletRequest request){
        User user=new User();
        userService.updateUserInfo(user);
        return null;
    }*/

}
