package com.xsdkj.wechat.controller;

import com.xsdkj.wechat.common.JsonResult;
import com.xsdkj.wechat.dto.UpdateSignDateDto;
import com.xsdkj.wechat.service.SystemService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author tiankong
 * @date 2020/1/12 18:21
 */
@RestController
@RequestMapping("/system")
public class SystemController {
    @Resource
    private SystemService systemService;

    /**
     * 获取签到相关的系统参数
     *
     * @return JsonResult
     */
    @GetMapping("/signDate")
    public JsonResult signDate() {
        return JsonResult.success(systemService.getSinDate());
    }

    /**
     * 修改签到相关的参数
     *
     * @return JsonResult
     */
    @PostMapping("/updateSignDate")
    public JsonResult updateSignDate(@RequestBody UpdateSignDateDto updateSignDateDto) {
        return JsonResult.success(systemService.updateSignDate(updateSignDateDto));
    }
}
