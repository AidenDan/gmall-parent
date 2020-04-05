package com.gmall.config;

import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author Aiden
 * @version 1.0
 * @description 配置jedisPool连接池注入到容器中，交给spring管理，通过注入jedisPool来获取jedis客户端对象
 * @date 2020-3-5 11:31:24
 */
@Configuration
public class AppJedisConfig {

    @Bean
    public JedisPool jedisPoolConfig(RedisProperties properties) throws Exception {
        //1、连接工厂中所有信息都有。
        JedisPoolConfig config = new JedisPoolConfig();

        RedisProperties.Pool pool = properties.getJedis().getPool();

        //这些配置
        config.setMaxIdle(pool.getMaxIdle());
        config.setMaxTotal(pool.getMaxActive());

        JedisPool jedisPool = null;
        jedisPool = new JedisPool(config, properties.getHost(), properties.getPort());
        return jedisPool;
    }
}
