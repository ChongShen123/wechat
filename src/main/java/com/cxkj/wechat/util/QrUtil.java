package com.cxkj.wechat.util;

import cn.hutool.core.io.FileUtil;
import cn.hutool.extra.qrcode.QrCodeUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 二维码工具
 *
 * @author tiankong
 * @date 2019/12/11 14:33
 */
@Component
public class QrUtil {
    @Value("${file.root-path}")
    private String rootPath;
    @Value("${file.qr-path}")
    private String qrPath;

    public String generate(String username) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("y/M/d/");
        String folder = qrPath + simpleDateFormat.format(new Date());
        // 实际的文件夹
        String realFolder = rootPath + folder;
        if (!FileUtil.exist(realFolder)) {
            FileUtil.mkdir(realFolder);
        }
        // 需要返回的文件名
        String fileName = folder + username + System.currentTimeMillis() + ".png";
        QrCodeUtil.generate(username, 300, 300, FileUtil.file(rootPath + fileName));
        return fileName;
    }
}
