package com.gmall.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author Aiden
 * @version 1.0
 * @description   把配置中自定义的属性读取进来
 * @date 2020-3-11 15:44:39
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "sso.server")
public class SsoConfig {
    private String url;
    private String loginpath;
}
