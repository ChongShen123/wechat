package com.xsdkj.wechat;

import cn.hutool.core.lang.Snowflake;
import com.xsdkj.wechat.util.QrUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author tiankong
 */
@EnableCaching
@EnableScheduling
@SpringBootApplication
public class WechatApplication {

    public static void main(String[] args) {
        SpringApplication.run(WechatApplication.class, args);
    }

    @Bean
    public Snowflake snowflake() {
        return new Snowflake(4, 13);
    }

    @Bean
    public QrUtil qrUtil() {
        return new QrUtil();
    }
}
