package com.gmall.config;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Aiden
 * @version 1.0
 * @description
 * @date 2020-3-15 19:45:49
 */
@Configuration
public class MYRabbitMQConfig {
      /* *
      *  程序启动就会自动执行这个配置类，自动创建队列，和交换机，如果原来已经有了就不会创建了
      *
      * */

    //创建队列
   @Bean
    public Queue createQueue(){
        Queue queue = new Queue("myqueue02", true, false, false, null);
        System.out.println("创建队列完成。。。");
        return queue;
    }

    //创建交换机
    @Bean
    public DirectExchange createExchange(){
        DirectExchange directExchange = new DirectExchange("myexchange02", true, false, null);
        System.out.println("创建交换机完成。。。");
        return directExchange;
    }

    //创建绑定关系
    @Bean
    public Binding bind(){
        //public Binding(String destination,  目的地即为队列名称
        // Binding.DestinationType destinationType, 目的地类型就是队列
        // String exchange, 交换机名称
        // String routingKey, 路由键名称 bindingkey的名称
        // @Nullable Map<String, Object> arguments)  参数
        Binding binding = new Binding("myqueue02",
                Binding.DestinationType.QUEUE,
                "myexchange02",
                "hihi",
                null);
        System.out.println("绑定关系创建完成。。。");
        return binding;
    }
}
