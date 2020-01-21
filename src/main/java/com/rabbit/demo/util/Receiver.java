package com.rabbit.demo.util;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.io.IOException;

@Component
@EnableTransactionManagement(proxyTargetClass = true)
public class Receiver {

    @RabbitListener(queues = "testQueue1")
    public String processMessageTestQueue1(String msg, Channel channel, Message message) {
        try {
            System.out.println(Thread.currentThread().getName() + " 接收到来自testQueue1队列的消息：" + msg);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return msg.toUpperCase();
    }

    @RabbitListener(queues = "testQueue2")
    public String processMessageTestQueue2(String msg, Channel channel, Message message) {
        try {
            System.out.println(Thread.currentThread().getName() + " 接收到来自testQueue2队列的消息：" + msg);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return msg.toUpperCase();
    }

    @RabbitListener(queues = "hello")
    public void processMessageHello(String msg, Channel channel, Message message) {
        try {
            System.out.println(Thread.currentThread().getName() + "接收到来自hello队列的消息: " + msg);
            //告诉服务器收到这条消息 已经被我消费了 可以在队列删掉 这样以后就不会再发了 否则消息服务器以为这条消息没处理掉 后续还会在发
            //丢弃这条消息,basicReject一次只能拒绝接收一个消息，而basicNack方法可以支持一次0个或多个消息的拒收，并且也可以设置是否requeue。
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
//            channel.basicReject(message.getMessageProperties().getDeliveryTag(), false);
//            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
            System.out.println("receiver success");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("receiver fail");
        }
    }

    @RabbitListener(queues = "helloObj")
    public void processMessageHelloObj(String msg, Channel channel, Message message) {
        try {
            System.out.println(Thread.currentThread().getName() + "接收到来自helloObj队列的消息: " + msg);
            //告诉服务器收到这条消息 已经被我消费了 可以在队列删掉 这样以后就不会再发了 否则消息服务器以为这条消息没处理掉 后续还会在发
            //丢弃这条消息,basicReject一次只能拒绝接收一个消息，而basicNack方法可以支持一次0个或多个消息的拒收，并且也可以设置是否requeue。
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
//            channel.basicReject(message.getMessageProperties().getDeliveryTag(), false);
//            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
            System.out.println("receiver success");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("receiver fail");
        }
    }

//    /**
//     * 监听对象传输
//     *
//     * @param obj
//     * @throws Exception
//     */
//    @RabbitListener(queues = "helloObj")
//    public void processMessageHelloObj(MessageObj obj, Channel channel, Message message) {
//        try {
//            System.out.println(obj);
//            System.out.println("messages ：" + obj.toString());
//            System.out.println(Thread.currentThread().getName() + "接收到来自helloObj队列的消息: " + obj);
//            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}