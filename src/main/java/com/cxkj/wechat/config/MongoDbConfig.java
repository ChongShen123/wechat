package com.cxkj.wechat.config;

import com.mongodb.MongoClientOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author tiankong
 * @date 2019/12/15 20:02
 */
@Configuration
public class MongoDbConfig {
    @Bean
    public MongoClientOptions mongoOptions() {
        // https://www.cnblogs.com/linzhanfly/p/9674778.html
        return MongoClientOptions
                .builder()
                .maxConnectionIdleTime(60000)
                .build();
    }
}
