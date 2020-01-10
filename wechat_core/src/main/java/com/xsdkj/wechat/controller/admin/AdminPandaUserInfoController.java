package com.xsdkj.wechat.controller.admin;
import com.xsdkj.wechat.common.JsonResult;
import com.xsdkj.wechat.service.AdminUserInfoService;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;

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
        System.out.println("123456:"+platformId);
        System.out.println(adminUserInfoService.selectRegisterCount(platformId));
        return JsonResult.success(adminUserInfoService.selectRegisterCount(platformId));

    }

    /**
     * 今日登陆人数
     */
    @GetMapping("/selectLastLoginTimes")
    public JsonResult selectLastLoginTimes(@RequestParam("platformId")  Integer platformId){
        System.out.println("123456:"+platformId);
        System.out.println(adminUserInfoService.selectLastLoginTimes(platformId));
        return JsonResult.success(adminUserInfoService.selectLastLoginTimes(platformId));
    }

    /**
     * 在线人数
     */
    @GetMapping("/selectOnlineUser")
    public JsonResult selectOnlineUser(@RequestParam("platformId")  Integer platformId){
        System.out.println(adminUserInfoService.selectOnlineUser(platformId));
        return JsonResult.success(adminUserInfoService.selectOnlineUser(platformId));
    }
    /**
     * 展示用户属性
     */
    @GetMapping("/selectUserInfo")
    public JsonResult selectUserInfo(@RequestParam("id") Integer id){
        System.out.println("aaaaaaaaaaaaaa+"+adminUserInfoService.selectUserInfo(id));
        return JsonResult.success(adminUserInfoService.selectUserInfo(id));
    }
    /**
     * 查看用户登陆日志
     */
    @GetMapping("/selectUserLoginLog")
    public JsonResult selectUserLoginLog(@RequestParam("id") Integer id){
        System.out.println("aaaaaaaaaaaaaa+"+adminUserInfoService.selectUserInfo(id));
        return JsonResult.success(adminUserInfoService.selectUserLoginLog(id));
    }





}
