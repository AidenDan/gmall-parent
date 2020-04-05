package com.gmall;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

/* *
* 使用rabbitmq的步骤
* 1、导入amqp-starter
* 2、编写自动配置
* 3、开启rabbitmq
*
* */
@EnableScheduling
@EnableRabbit
@SpringBootApplication
public class RabbitTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(RabbitTestApplication.class, args);
    }

    //修改序列化方式，使其序列化为json
    @Bean
    public MessageConverter  getMessageConverter(){
        return new Jackson2JsonMessageConverter();
    }

}
