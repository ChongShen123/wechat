package com.cxkj.wechat.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author tiankong
 * @date 2019/12/21 15:29
 */
@Controller
@RequestMapping("/admin")
public class TestController {
    @GetMapping("/test")
    public String test() {
        return "test";
    }
}
