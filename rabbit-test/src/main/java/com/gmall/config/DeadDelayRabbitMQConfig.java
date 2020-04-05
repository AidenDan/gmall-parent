package com.gmall.config;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Aiden
 * @version 1.0
 * @description  创建死信队列
 * @date 2020-3-15 19:45:49
 */
@Configuration
public class DeadDelayRabbitMQConfig {
      /* *
      *  程序启动就会自动执行这个配置类，自动创建队列，和交换机，如果原来已经有了就不会创建了
      *
      * */

    //创建死信队列  死信队列是没有消费者的
   @Bean(name = "delayQueue")
    public Queue createDeadDelayQueue(){
       //设置死信队列的参数
       Map<String, Object> arguments = new HashMap<>();
       //1、这个队列中所有消息的过期时间
       //2、消息死了后交给哪个交换机
       //3、死信发出去后交给哪个队列的路由键
       arguments.put("x-message-ttl", 100 * 1000);
       arguments.put("x-dead-letter-exchange", "receive-delayMessage-exchange");
       arguments.put("x-dead-letter-routing-key", "receive-delayMessage");
       Queue queue = new Queue("dead-delay-queue", true, false, false, arguments);
        System.out.println("创建死信队列完成。。。");
        return queue;
    }

    //创建交换机
    @Bean(name = "delayExchange")
    public DirectExchange createDelayExchange(){
        DirectExchange directExchange = new DirectExchange("dead-delay-exchange", true, false, null);
        System.out.println("创建交换机完成。。。");
        return directExchange;
    }

    //创建绑定关系
    @Bean(name = "delayBind")
    public Binding delayBind(){
        //public Binding(String destination,  目的地即为队列名称
        // Binding.DestinationType destinationType, 目的地类型就是队列
        // String exchange, 交换机名称
        // String routingKey, 路由键名称 bindingkey的名称
        // @Nullable Map<String, Object> arguments)  参数
        Binding binding = new Binding("dead-delay-queue",
                Binding.DestinationType.QUEUE,
                "dead-delay-exchange",
                "dead-delay",
                null);
        System.out.println("绑定关系创建完成。。。");
        return binding;
    }

    //创建一个普通的队列用于接收死信队列中的消息
    @Bean
    public Queue createQueue2(){
        Queue queue = new Queue("receive-delayMessage-queue", true, false, false, null);
        System.out.println("创建队列完成。。。");
        return queue;
    }

    //创建交换机
    @Bean
    public DirectExchange createExchange2(){
        DirectExchange directExchange = new DirectExchange("receive-delayMessage-exchange", true, false, null);
        System.out.println("创建交换机完成。。。");
        return directExchange;
    }

    //创建绑定关系
    @Bean
    public Binding bind2(){
        //public Binding(String destination,  目的地即为队列名称
        // Binding.DestinationType destinationType, 目的地类型就是队列
        // String exchange, 交换机名称
        // String routingKey, 路由键名称 bindingkey的名称
        // @Nullable Map<String, Object> arguments)  参数
        Binding binding = new Binding("receive-delayMessage-queue",
                Binding.DestinationType.QUEUE,
                "receive-delayMessage-exchange",
                "receive-delayMessage",
                null);
        System.out.println("绑定关系创建完成。。。");
        return binding;
    }
}
