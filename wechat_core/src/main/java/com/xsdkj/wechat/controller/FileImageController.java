package com.xsdkj.wechat.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ArrayUtil;
import com.xsdkj.wechat.common.JsonResult;
import com.xsdkj.wechat.common.SystemConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

/**
 * @author tiankong
 * @date 2019/12/13 14:46
 */
@RestController
@RequestMapping("/file")
@Slf4j
public class FileImageController {
    @Value("${file.root-path}")
    private String rootPath;
    @Value("${file.img-path}")
    private String imgPath;
    @Value("${file.voice-path}")
    private String voicePath;

    /**
     * 上传图片
     */
    @PostMapping("/upload/img")
    @ResponseBody
    public JsonResult uploadIcon(@RequestParam("file") MultipartFile file, HttpServletRequest request) throws IOException {
        //如果大于1M 不让上传
        if (file.getSize() > SystemConstant.UPLOAD_FILE_SIZE){
            return JsonResult.failed("请上传小于1M的文件");
        }
        String suffix = getSuffix(Objects.requireNonNull(file.getOriginalFilename()));
        if (!ArrayUtil.contains(SystemConstant.SUFFIX_ARRAY, suffix)) {
            return JsonResult.failed("图片格式不正确,支持格式为:" + Arrays.toString(SystemConstant.SUFFIX_ARRAY));
        }
        // 文件夹
        String folder = imgPath + new SimpleDateFormat("y/M/d/").format(new Date());
        // 真实文件夹地址
        String realFolder = rootPath + folder;
        if (!FileUtil.exist(realFolder)) {
            FileUtil.mkdir(realFolder);
        }
        // 返回的文件名
        String fileName = folder + System.currentTimeMillis() + ".png";
        // 真实文件名
        String realName = rootPath + fileName;
        upload(file, realName);
        return JsonResult.success(fileName);
    }

    /**
     * 上传音频
     */
    @PostMapping("/upload/voice")
    @ResponseBody
    public JsonResult uploadVoice(@RequestParam("file") MultipartFile file, HttpServletRequest request) throws IOException {
        //如果大于1M 不让上传
        if (file.getSize() > SystemConstant.UPLOAD_FILE_SIZE){
            return JsonResult.failed("请上传小于1M的文件");
        }
        // 文件夹
        String folder = imgPath + new SimpleDateFormat("y/M/d/").format(new Date());
        // 真实文件夹地址
        String realFolder = rootPath + folder;
        if (!FileUtil.exist(realFolder)) {
            FileUtil.mkdir(realFolder);
        }
        // 返回的文件名
        String fileName = folder + System.currentTimeMillis() + ".png";
        // 真实文件名
        String realName = rootPath + fileName;
        upload(file, realName);
        return JsonResult.success(fileName);
    }

    private String getSuffix(String originalFilename) {
        return originalFilename.substring(originalFilename.lastIndexOf("."));
    }

    private void upload(MultipartFile file, String fileName) throws IOException {
        File newFile = new File(fileName);
        try {
            file.transferTo(newFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
