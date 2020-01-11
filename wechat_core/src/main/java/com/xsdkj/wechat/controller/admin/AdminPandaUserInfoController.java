package com.xsdkj.wechat.controller.admin;
import com.xsdkj.wechat.common.JsonResult;
import com.xsdkj.wechat.dto.UserGroupDto;
import com.xsdkj.wechat.dto.UserLoginLogoDto;
import com.xsdkj.wechat.dto.UserMoodDto;
import com.xsdkj.wechat.dto.UserWalletDto;
import com.xsdkj.wechat.service.AdminUserInfoService;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;

/**
 * @author panda
 * 后台统计平台用户属性
 */
@RestController
@RequestMapping("/AdminUserInfo")
public class AdminPandaUserInfoController {
    @Resource
    AdminUserInfoService adminUserInfoService;
    /**
     * 注册人数
     */
    @GetMapping("/selectAdminRegister")
    public JsonResult selectAdminRegister( @RequestParam("platformId") Integer platformId){
        return JsonResult.success(adminUserInfoService.selectRegisterCount(platformId));

    }

    /**
     * 今日登陆人数
     */
    @GetMapping("/selectLastLoginTimes")
    public JsonResult selectLastLoginTimes(@RequestParam("platformId")  Integer platformId){
        return JsonResult.success(adminUserInfoService.selectLastLoginTimes(platformId));
    }

    /**
     * 在线人数
     */
    @GetMapping("/selectOnlineUser")
    public JsonResult selectOnlineUser(@RequestParam("platformId")  Integer platformId){
        return JsonResult.success(adminUserInfoService.selectOnlineUser(platformId));
    }
    /**
     * 展示用户属性
     */
    @GetMapping("/selectUserInfo")
    public JsonResult selectUserInfo(@RequestParam("id") Integer id){
        return JsonResult.success(adminUserInfoService.selectUserInfo(id));
    }
    /**
     * 查看用户登陆日志
     */
    @PostMapping("/selectUserLoginLog")
    public JsonResult selectUserLoginLog(@RequestBody UserLoginLogoDto userLoginLogoDto){
        return JsonResult.success(adminUserInfoService.selectUserLoginLog(userLoginLogoDto));
    }
    /**
     * 查看用户钱包
     */
    @PostMapping("/selectUserWallet")
    public JsonResult selectUserWallet(@RequestBody UserWalletDto userWalletDto){
        List<UserWalletDto> userWalletDtos = adminUserInfoService.selectUserWallet(userWalletDto);
        return JsonResult.success(userWalletDtos);
    }
    /**
     * 查看用户的朋友圈
     */
    @PostMapping("/selectUserMood")
    public JsonResult selectUserMood(@RequestBody UserMoodDto userMoodDto){
        return JsonResult.success(adminUserInfoService.selectUserMood(userMoodDto));
    }
    /**
     *查看所有的群组
     */
    @PostMapping("/selectUserGroup")
    public JsonResult selectUserGroup(@RequestBody  UserGroupDto userGroupDto){
        return JsonResult.success(adminUserInfoService.selectUsergroup(userGroupDto));
    }







}
