package com.rabbit.demo.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description: 在rabbitmq中，exchange有4个类型：direct，topic，fanout，header。
 * @Param:
 * @return:
 * @Author: wangliang
 * @Date: 2019/1/24
 */
@Configuration
public class RabbitConfig {

    /**
     * 创建路由
     *
     * @return
     */
    @Bean
    public TopicExchange tranExchange() {
        return new TopicExchange("tranExchange");
    }

    /**
     * 创建队列
     *
     * @return
     */
    @Bean
    public Queue QueueTran() {
        return new Queue("tranQueue", true);
    }

    /**
     * 绑定
     *
     * @param QueueTran
     * @param topicExchange
     * @return
     */
    @Bean
    public Binding bindingTran(Queue QueueTran, TopicExchange topicExchange) {
        return BindingBuilder.bind(QueueTran).to(topicExchange).with("routingKeyTran");
    }

    /**
     * 在direct类型的exchange中，只有这两个routingkey完全相同，exchange才会选择对应的binging进行消息路由。
     *
     * @return
     */
    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange("directExchange");
    }

    /**
     * 创建队列
     *
     * @return
     */
    @Bean
    public Queue QueueA() {
        return new Queue("hello", true);
    }

    /**
     * 创建队列
     *
     * @return
     */
    @Bean
    public Queue QueueB() {
        return new Queue("helloObj", true);
    }

    /**
     * 创建队列
     *
     * @return
     */
    @Bean
    public Queue queue1() {
        return new Queue("testQueue1", true); // true表示持久化该队列
    }

    /**
     * 创建队列
     *
     * @return
     */
    @Bean
    public Queue queue2() {
        return new Queue("testQueue2", true);
    }

    /**
     * Fanout 就是我们熟悉的广播模式或者订阅模式，给Fanout交换机发送消息，绑定了这个交换机的所有队列都收到这个消息。
     *
     * @return
     */
    @Bean
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange("ABExchange");
    }

    /**
     * 绑定
     *
     * @param QueueA
     * @param fanoutExchange
     * @return
     */
    @Bean
    public Binding bindingExchangeA(Queue QueueA, FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(QueueA).to(fanoutExchange);
    }

    /**
     * 绑定
     *
     * @param QueueB
     * @param fanoutExchange
     * @return
     */
    @Bean
    public Binding bindingExchangeB(Queue QueueB, FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(QueueB).to(fanoutExchange);
    }

    /**
     * topic exchange此类型exchange和上面的direct类型差不多，但direct类型要求routingkey完全相等，这里的routingkey可以有通配符：'*','#'.
     * 其中'*'表示匹配一个单词， '#'则表示匹配没有或者多个单词
     *
     * @return
     */
    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange("topicExchange");
    }

    /**
     * 绑定
     *
     * @param queue1
     * @param topicExchange
     * @return
     */
    @Bean
    public Binding binding1(Queue queue1, TopicExchange topicExchange) {
        return BindingBuilder.bind(queue1).to(topicExchange).with("routingKey1");
    }

    /**
     * 绑定
     *
     * @param queue2
     * @param topicExchange
     * @return
     */
    @Bean
    public Binding binding2(Queue queue2, TopicExchange topicExchange) {
        return BindingBuilder.bind(queue2).to(topicExchange).with("routingKey2");
    }
}