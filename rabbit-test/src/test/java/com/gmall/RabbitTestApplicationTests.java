package com.gmall;

import com.gmall.bean.User;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.Serializable;

@RunWith(SpringRunner.class)
@SpringBootTest
 class RabbitTestApplicationTests {
    @Autowired
    RabbitTemplate rabbitTemplate;
    @Autowired
    AmqpAdmin amqpAdmin;

    @Test
    void contextLoads() {
        //修改序列化方式，使其序列化为json
        //rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        User user = new User("王五", "ada@qq.com");
        rabbitTemplate.convertAndSend("direct-exchange","world",user);
        System.out.println("发送消息完成...");
    }

    //创建队列
    @Test
    public void createQueue(){
        Queue queue = new Queue("myqueue01", true, false, false);
        amqpAdmin.declareQueue(queue);
        System.out.println("创建队列完成。。。");
    }

    //创建交换机
    @Test
    public void createExchange(){
        DirectExchange directExchange = new DirectExchange("myexchange01", true, false);
        amqpAdmin.declareExchange(directExchange);
        System.out.println("创建交换机完成。。。");
    }

    //创建绑定关系
    @Test
    public void bind(){
        //public Binding(String destination,  目的地即为队列名称
        // Binding.DestinationType destinationType, 目的地类型就是队列
        // String exchange, 交换机名称
        // String routingKey, 路由键名称 bindingkey的名称
        // @Nullable Map<String, Object> arguments)  参数
        Binding binding = new Binding("myqueue01",
                Binding.DestinationType.QUEUE,
                "myexchange01",
                "hi",
                null);
        amqpAdmin.declareBinding(binding);
        System.out.println("绑定关系创建完成。。。");
    }

}
















