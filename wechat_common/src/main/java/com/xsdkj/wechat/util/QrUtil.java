package com.xsdkj.wechat.util;

import cn.hutool.core.io.FileUtil;
import cn.hutool.extra.qrcode.QrCodeUtil;
import org.springframework.beans.factory.annotation.Value;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 二维码工具
 *
 * @author tiankong
 * @date 2019/12/11 14:33
 */
public class QrUtil {
    @Value("${file.root-path}")
    private String rootPath;
    @Value("${file.qr-path}")
    private String qrPath;

    /**
     * 创建二维码
     *
     * @param content 二维码内容
     * @return 文件路径
     */
    public String generate(String content) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("y/M/d/");
        String folder = qrPath + simpleDateFormat.format(new Date());
        // 实际的文件夹
        String realFolder = rootPath + folder;
        if (!FileUtil.exist(realFolder)) {
            FileUtil.mkdir(realFolder);
        }
        // 需要返回的文件名
        String fileName = folder + content + System.currentTimeMillis() + ".png";
        QrCodeUtil.generate(content, 300, 300, FileUtil.file(rootPath + fileName));
        return fileName;
    }
}
