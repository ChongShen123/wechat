package com.xsdkj.wechat.controller.admin;


import cn.hutool.core.date.DateUtil;
import com.xsdkj.wechat.common.JsonResult;
import com.xsdkj.wechat.dto.UserLoginDto;
import com.xsdkj.wechat.service.UserService;
import com.xsdkj.wechat.util.LogUtil;
import com.xsdkj.wechat.vo.LoginVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author tiankong
 * @date 2019/12/21 15:29
 */
@Slf4j
@RestController
@RequestMapping("/admin")
public class AdminController {
    @Resource
    private UserService userService;
    @Value("${jwt.tokenHeader}")
    private String tokenHeader;

    @PostMapping("/login")
    public JsonResult login(@Validated @RequestBody UserLoginDto userLoginDto, HttpServletRequest request) {
        System.out.println(userLoginDto);
        long begin = System.currentTimeMillis();
        log.debug(LogUtil.INTERVAL);
        log.debug("开始处理登录请求...");
        Map<String, String> map = new HashMap<>(1);
        LoginVo login = userService.login(userLoginDto, request, true);
        map.put("token", login.getToken());
        JsonResult result = JsonResult.success(map);
        log.debug("登录请求处理完毕 {}ms", DateUtil.spendMs(begin));
        log.debug(LogUtil.INTERVAL);
        return result;
    }

    @GetMapping("/info")
    public JsonResult getInfo() {
        log.debug(LogUtil.INTERVAL);
        long begin = System.currentTimeMillis();
        log.debug("开始获取用户信息...");
        JsonResult success = JsonResult.success(userService.getInfo());
        log.debug("用户信息获取完毕 {}ms", DateUtil.spendMs(begin));
        log.debug(LogUtil.INTERVAL);
        return success;
    }

    @PostMapping("/logout")
    public JsonResult logout() {
        return JsonResult.success();
    }
}
