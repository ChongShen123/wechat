package com.xsdkj.wechat.config;

import com.mongodb.MongoClientOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author tiankong
 * @date 2020/1/7 17:58
 */
@Configuration
public class MongoDbConfig {
    @Bean
    public MongoClientOptions mongoClientOptions() {
        return MongoClientOptions.builder()
                .maxConnectionIdleTime(60000)
                .build();
    }
}
