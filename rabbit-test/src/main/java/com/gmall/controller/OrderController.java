package com.gmall.controller;

import com.gmall.bean.Order;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;
import java.util.UUID;

/**
 * @author Aiden
 * @version 1.0
 * @description
 * @date 2020-3-15 20:06:59
 */
@RestController
public class OrderController {
    @Autowired
    RabbitTemplate rabbitTemplate;

    //模拟当客户订单每增加一件时，库存就减少一件
    @GetMapping("/order")
    public Object order(){
        Order order = new Order(new Random().nextInt(100), 999, "张三", UUID.randomUUID().toString().replace("-", ""));
        //往消息队列中发消息
        //rabbitTemplate.convertAndSend("myexchange02", "hihi", order);
        //往死信消息队列中发消息
        rabbitTemplate.convertAndSend("dead-delay-exchange", "dead-delay", order);
        return order;
    }
}
