package com.gmall.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

/**
 * @author Aiden
 * @version 1.0
 * @description
 * @date 2020-3-5 13:39:31
 */
@Configuration
public class RedissonConfig {

    /* *
    * Redisson的配置
    * */
    @Bean
    RedissonClient redissonClient() throws IOException{
        Config config = new Config();
        config.useSingleServer().setAddress("redis://localhost:6379");
                //.addNodeAddress("redis://localhost:6379");
        return Redisson.create(config);
    }
}
