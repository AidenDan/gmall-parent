package com.gmall.pms.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.net.UnknownHostException;

/**
 * @author Aiden
 * @version 1.0
 * @description redis会默认把存入的数据序列化为二进制，不便于阅读也不便于跨语言，我们可以自定义序列化机制，把它序列化为可
 * 跨语言的json格式
 * @date 2020-3-1 19:12:18
 */
@Configuration
public class PmsRedisConfig {

    @Bean("redisTemplate")
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) throws UnknownHostException {
        RedisTemplate<Object, Object> template = new RedisTemplate();
        template.setConnectionFactory(redisConnectionFactory);
        //修改默认的序列化方式，序列化为json
        template.setDefaultSerializer(new GenericJackson2JsonRedisSerializer());
        return template;
    }
}
