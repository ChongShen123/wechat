package com.xsdkj.wechat.controller.admin;

import com.xsdkj.wechat.common.JsonPage;
import com.xsdkj.wechat.common.JsonResult;
import com.xsdkj.wechat.dto.ListUserDto;
import com.xsdkj.wechat.service.UserService;
import com.xsdkj.wechat.vo.UserVo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author tiankong
 * @date 2020/1/9 14:23
 */
@RestController
@RequestMapping("/admin/")
public class SkyUserController {
    @Resource
    private UserService userService;

    /**
     * 条件查询用户
     *
     * @param listUserDto 参数
     * @return JsonResult
     */
    @PostMapping("/listUser")
    public JsonResult listUser(@RequestBody ListUserDto listUserDto) {
        List<UserVo> list = userService.listUser(listUserDto);
        return JsonResult.success(JsonPage.restPage(list));
    }
}
