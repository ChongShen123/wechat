package com.cxkj.wechat.controller.admin;

import com.cxkj.wechat.dto.UserLoginDto;
import com.cxkj.wechat.service.AdminService;
import com.cxkj.wechat.service.UserService;
import com.cxkj.wechat.util.JsonResult;
import com.cxkj.wechat.vo.LoginVo;
import com.cxkj.wechat.vo.admin.LoginInfoVo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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

//    @GetMapping("/index")
//    public ModelAndView test(ModelAndView model) {
//        model.addObject("name", "sky");
//        model.setViewName("index");
//        return model;
//    }
//
//    @GetMapping("/login")
//    public ModelAndView login() {
//        return console("user/login");
//    }
//
//    @GetMapping("/logout")
//    public void logout(HttpServletResponse response) {
//        try {
//            response.sendRedirect("/admin/login");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @GetMapping("/path")
//    public ModelAndView console(String url, ModelAndView model) {
//        model.setViewName(url);
//        return model;
//    }
//
//    private ModelAndView console(String url) {
//        ModelAndView modelAndView = new ModelAndView();
//        modelAndView.setViewName(url);
//        return modelAndView;
//    }
}
