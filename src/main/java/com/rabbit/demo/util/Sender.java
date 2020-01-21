package com.rabbit.demo.util;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.UUID;

@Component
public class Sender
        implements
        RabbitTemplate.ConfirmCallback,
        RabbitTemplate.ReturnCallback {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 类似 service init
     */
    @PostConstruct
    public void init() {
        rabbitTemplate.setConfirmCallback(this);
        rabbitTemplate.setReturnCallback(this);
//        rabbitTemplate.setChannelTransacted(true);//配置事务
        rabbitTemplate.setMandatory(true);
    }

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        if (ack) {
            System.out.println("生产者消息发送成功:" + correlationData);
        } else {
            System.out.println("生产者消息发送失败:" + cause);
        }
    }

    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        System.out.println(message.getMessageProperties().getCorrelationId() + " 发送失败");
    }

    //发送消息，不需要实现任何接口，供外部调用。
    @Transactional
    public void convertSendAndReceive1(String msg) {
        CorrelationData correlationId = new CorrelationData(UUID.randomUUID().toString());
        System.out.println("开始发送消息 : " + msg.toLowerCase());
        rabbitTemplate.setChannelTransacted(false);//配置事务
        String response = rabbitTemplate.convertSendAndReceive("topicExchange", "routingKey1", msg, correlationId).toString();
        System.out.println("结束发送消息 : " + msg.toLowerCase());
        System.out.println("消费者响应 : " + response + " 消息处理完成");
    }

    //发送消息，不需要实现任何接口，供外部调用。
    @Transactional
    public void convertSendAndReceive2(String msg) {
        CorrelationData correlationId = new CorrelationData(UUID.randomUUID().toString());
        System.out.println("开始发送消息 : " + msg.toLowerCase());
        rabbitTemplate.setChannelTransacted(false);//配置事务
        String response = rabbitTemplate.convertSendAndReceive("topicExchange", "routingKey2", msg, correlationId).toString();
        System.out.println("结束发送消息 : " + msg.toLowerCase());
        System.out.println("消费者响应 : " + response + " 消息处理完成");
    }

    @Transactional
    public void send1() {
        String context = "你好现在是 " + new Date() + "send1";
        System.out.println("send1发送内容 : " + context);
        CorrelationData correlationId = new CorrelationData(UUID.randomUUID().toString());
        rabbitTemplate.setChannelTransacted(false);//配置事务
        //        this.rabbitTemplate.convertAndSend("hello", context);
        this.rabbitTemplate.convertAndSend("ABExchange", "", context, correlationId);
//        System.out.println("消费者响应 : " + response + " 消息处理完成");
    }

    @Transactional(rollbackFor = Exception.class)
    public void send2() {
        String context = "你好现在是 " + new Date() + "send2";
//        System.out.println(1 / 0);
        System.out.println("send2发送内容 : " + context);
        CorrelationData correlationId = new CorrelationData(UUID.randomUUID().toString());
//        this.rabbitTemplate.convertAndSend("hello", context);
//        this.rabbitTemplate.convertAndSend("ABExchange", "", context);
        rabbitTemplate.setChannelTransacted(false);//配置事务
        this.rabbitTemplate.convertAndSend("ABExchange", "", context, correlationId);
//        System.out.println("消费者响应 : " + response + " 消息处理完成");
//        System.out.println(1 / 0);
    }

    @Transactional(rollbackFor = Exception.class)
    public void send3() {
        String context = "你好现在是 " + new Date() + "send3";
        System.out.println("send3发送内容 : " + context);
        CorrelationData correlationId = new CorrelationData(UUID.randomUUID().toString());
//        this.rabbitTemplate.convertAndSend("hello", context);
//        this.rabbitTemplate.convertAndSend("ABExchange", "", context);
        rabbitTemplate.setChannelTransacted(true);//配置事务
        this.rabbitTemplate.convertAndSend("tranExchange", "routingKeyTran", context, correlationId);
//        System.out.println(1 / 0);
//        System.out.println("消费者响应 : " + response + " 消息处理完成");
    }

    /**
     //     * 发送对象消息,可以成功发送和接收
     //     */
//    @Transactional
//    public void sendObj() {
//        MessageObj obj = new MessageObj();
//        obj.setACK(false);
//        obj.setId(123);
//        obj.setName("zhangsan");
//        obj.setValue("data");
//        System.out.println("发送 : " + obj);
//        rabbitTemplate.setChannelTransacted(false);//配置事务
//        this.rabbitTemplate.convertAndSend("helloObj", obj);
////        CorrelationData correlationId = new CorrelationData(UUID.randomUUID().toString());
////        this.rabbitTemplate.convertSendAndReceive("topicExchange", "routingKey1", FastJsonConvertUtil.toJsonObject(obj), correlationId);
////        this.rabbitTemplate.convertAndSend("ABExchange", "", obj);
//    }
}