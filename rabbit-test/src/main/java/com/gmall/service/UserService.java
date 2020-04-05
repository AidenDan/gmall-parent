package com.gmall.service;

import com.gmall.bean.Order;
import com.gmall.bean.User;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @author Aiden
 * @version 1.0
 * @description
 * @date 2020-3-15 17:02:23
 */
/* *
* 消息确认机制：
* 1、如果这个消息收到了，在处理期间、出现了运行时异常，默认认为消息没有被正确处理
* 那么消息的状态就是uback,队列中感知到有一个unack的消息
* unack的消息队列会再次尝试把这个消息发给其他消费者
* 2、我们不要默认的unack机制，我们不让他认为到底是ack还是unack。我们使用手动确认机制
* 否则：场景
*        我们收到了消息，并且库存扣了，但是出现了未知的异常，导致消息又重新入队了，
* 这个消息被不断地重复发给我们；
* 解决方法：
* 1、手动ack--->最终使用的方法
* 处理消息，回复成功
* channel.basicAck();
* //消费消息，失败出现异常时，就拒绝消息
* channel.basicNack/Reject();
* 2.接口幂等性：在本地维护一个日志表，记录哪些会员哪些商品哪个订单已经减过库存，再来同样的消息就不减了
*
*
*
*
*
*
*
*
* */
@Service
public class UserService {


    /* *
        TCP连接，长连接，不会断，但是channel会断
    * 监听接受消息
    *  输入监听的队列
    *  1、消息封装在Message对象中
    *  2、可直接根据消息类型接受消息，直接写这个参数类型，如User
    *  3、还可以写Channel      4、3个参数个数可以任意写
    */
    @RabbitListener(queues = {"world"})
    public  void getMessage(Message message, User user, Channel channel) throws IOException {
        byte[] body = message.getBody();
        System.out.println(body);
        System.out.println(user.toString());
        MessageProperties messageProperties = message.getMessageProperties();
        System.out.println(messageProperties);
        System.out.println("监听到的消息--->"+message);

        //可以把消息拒绝掉，让rabbitMq在把消息发送给别的队列  false表示当前消息不再入队列，true表示当前消息继续加入队列
        //channel.basicReject(messageProperties.getDeliveryTag(), false);
    }

    @RabbitListener(queues = {"myqueue02"})
    public  void getOrderMessage(Message message, Order order, Channel channel) throws IOException {
        byte[] body = message.getBody();
        System.out.println(body);
        System.out.println(order.toString());
        MessageProperties messageProperties = message.getMessageProperties();
        System.out.println(messageProperties);
        System.out.println("监听到的消息--->"+message);
        //ack机制测试
        //当发生unack时，消息会重新进入队列
        if(order.getSkuId()%2==0){
            //消息处理失败的手动Unack
            //false表示只处理本条信息，true表示这条信息重新入队
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
            throw new RuntimeException("消息接收异常。。。");
        }
        //可以把消息拒绝掉，让rabbitMq在把消息发送给别的队列  false表示当前消息不再入队列，true表示当前消息继续加入队列
        //channel.basicReject(messageProperties.getDeliveryTag(), false);
        //手动回复成功  false表示只(回复)处理本条消息
        //如果在手动回复的设置下，而我们又不去手动回复ack，那么rabbitmq就会认为消费者还在处理这条信息，并不会把这条消息重新入队
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }

    /* *
    * 监听被死信队列转发的消息
    * */
    @RabbitListener(queues = {"receive-delayMessage-queue"})
    public  void getDeadMessage(Message message, Order order, Channel channel) throws IOException {
        byte[] body = message.getBody();
        System.out.println(body);
        System.out.println(order.toString());
        MessageProperties messageProperties = message.getMessageProperties();
        System.out.println(messageProperties);
        System.out.println("监听到的关闭订单的消息--->"+message);
        //如果在手动回复的设置下，而我们又不去手动回复ack，那么rabbitmq就会认为消费者还在处理这条信息，并不会把这条消息重新入队
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);

        //可以把消息拒绝掉，让rabbitMq在把消息发送给别的队列  false表示当前消息不再入队列，true表示当前消息继续加入队列
        //channel.basicReject(messageProperties.getDeliveryTag(), false);
    }
}
