package com.xsdkj.wechat.controller;

import com.xsdkj.wechat.common.JsonResult;
import com.xsdkj.wechat.dto.UpdateSignAwardDto;
import com.xsdkj.wechat.service.SignAwardService;
import com.xsdkj.wechat.vo.SignAwardVo;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author tiankong
 * @date 2020/1/14 14:57
 */
@RequestMapping("/signAward")
@RestController
public class UserSignAwardController {
    @Resource
    private SignAwardService signAwardService;

    @GetMapping
    public JsonResult listAll() {
        List<SignAwardVo> list = signAwardService.listAll();
        return JsonResult.success(list);
    }

    @PutMapping
    public JsonResult updateSignAward(@Validated @RequestBody UpdateSignAwardDto updateSignAwardDto) {
        signAwardService.updateSignAward(updateSignAwardDto);
        return JsonResult.success();
    }
}
