package com.xsdkj.wechat.controller.admin;


import com.xsdkj.wechat.common.JsonResult;
import com.xsdkj.wechat.dto.UserLoginDto;
import com.xsdkj.wechat.service.UserService;
import com.xsdkj.wechat.vo.LoginVo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author tiankong
 * @date 2019/12/21 15:29
 */
@RestController
@RequestMapping("/admin")
public class AdminController {

    @Resource
    private UserService userService;
    @Value("${jwt.tokenHeader}")
    private String tokenHeader;

    @PostMapping("/login")
    public JsonResult login(@RequestBody UserLoginDto userLoginDto, HttpServletRequest request) {
        Map<String, String> map = new HashMap<>();
        LoginVo login = userService.login(userLoginDto, request, true);
        map.put("token", login.getToken());
        return JsonResult.success(map);
    }
    @GetMapping("/info")
    public JsonResult getInfo() {
        return JsonResult.success(userService.getInfo());
    }

    @PostMapping("/logout")
    public JsonResult logout() {
        return JsonResult.success();
    }
}
