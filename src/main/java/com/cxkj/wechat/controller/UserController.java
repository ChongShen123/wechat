package com.cxkj.wechat.controller;


import com.cxkj.wechat.dto.UserLoginDto;
import com.cxkj.wechat.dto.UserRegisterDto;

import com.cxkj.wechat.dto.UserUpdateInfoParam;
import com.cxkj.wechat.dto.UserUpdatePassword;
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
    public JsonResult register(@Validated @RequestBody UserRegisterDto param, HttpServletRequest request) {
        return JsonResult.success(userService.register(param, request));
    }

    @PostMapping("/login")
    public JsonResult login(@Validated @RequestBody UserLoginDto param, HttpServletRequest request) {
        return JsonResult.success(userService.login(param, request, true));
    }
    @PostMapping("/update")
    public JsonResult updateUserInfo(@RequestBody UserUpdateInfoParam param ){
        userService.updateUserInfo(param);
        return JsonResult.success();
    }
    @PostMapping("updatePassWord")
    public JsonResult  updatePassWord(@RequestBody UserUpdatePassword password){
        userService.updatePassWord(password);
        return JsonResult.success();
    }


}
