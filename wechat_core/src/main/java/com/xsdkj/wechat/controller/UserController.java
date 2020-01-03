package com.xsdkj.wechat.controller;


import com.xsdkj.wechat.common.JsonResult;
import com.xsdkj.wechat.dto.UserLoginDto;
import com.xsdkj.wechat.dto.UserRegisterDto;
import com.xsdkj.wechat.dto.UserUpdateInfoParam;
import com.xsdkj.wechat.dto.UserUpdatePassword;
import com.xsdkj.wechat.service.UserService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        userService.register(param, request);
        return JsonResult.success();
    }

    @PostMapping("/login")
    public JsonResult login(@Validated @RequestBody UserLoginDto param, HttpServletRequest request) {
        return JsonResult.success(userService.login(param, request, true));
    }

    @PostMapping("/update")
    public JsonResult updateUserInfo(@RequestBody UserUpdateInfoParam param) {
        userService.updateUserInfo(param);
        return JsonResult.success();
    }

    @PostMapping("/updatePassWord")
    public JsonResult updatePassWord(@RequestBody UserUpdatePassword password) {
        userService.updatePassword(password);
        return JsonResult.success();
    }


}
