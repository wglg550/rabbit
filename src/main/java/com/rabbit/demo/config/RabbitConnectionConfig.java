package com.rabbit.demo.config;

import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.RabbitConnectionFactoryBean;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;


/**
 * <p><b>Description:</b> Rabbit MQ连接工厂配置
 * <p><b>Company:</b>
 *
 * @author created by hongda at 11:33 on 2017-07-05
 * @version V0.1
 */
@Configuration
@EnableRabbit
public class RabbitConnectionConfig {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    //将配置提出来，方便apollo配置中心,或做灵活配置
    @Value("${spring.rabbitmq.host}")
    String host;

    @Value("${spring.rabbitmq.port}")
    int port;

    @Value("${spring.rabbitmq.username}")
    String username;

    @Value("${spring.rabbitmq.password}")
    String password;

//    @Value("${spring.rabbitmq.connection-timeout}")
//    int connectionTimeout;
//
//    @Value("${spring.rabbitmq.template.receive-timeout}")
//    int receiveTimeout;

    @Value("${spring.rabbitmq.virtual-host}")
    String virtualHost;

    @Value("${spring.rabbitmq.cache.channel.size}")
    int cacheSize;

    @Value("${spring.rabbitmq.publisher-confirms}")
    boolean publisherConfirms;

    @Value("${spring.rabbitmq.publisher-returns}")
    boolean publisherReturns;

    /**
     * 创建RabbitMQ连接工厂
     *
     * @param
     * @return CachingConnectionFactory
     * @throws Exception 异常
     */
    @Bean
    public CachingConnectionFactory rabbitConnectionFactory() throws Exception {
        logger.info("==> custom rabbitmq connection factory");
        RabbitConnectionFactoryBean factory = new RabbitConnectionFactoryBean();
        factory.setHost(host);
        factory.setPort(port);
        factory.setUsername(username);
        factory.setPassword(password);
        factory.setVirtualHost(virtualHost);
        factory.setAutomaticRecoveryEnabled(true);
        //factory.setConnectionTimeout(connectionTimeout);
//        factory.setAutomaticRecoveryEnabled(true);
        factory.afterPropertiesSet();
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(factory.getObject());
        connectionFactory.setPublisherReturns(publisherReturns);
        connectionFactory.setPublisherConfirms(publisherConfirms);
        connectionFactory.setChannelCacheSize(cacheSize);
        return connectionFactory;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        logger.info("==> custom rabbitmq Listener factory:" + connectionFactory);
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setConcurrentConsumers(3);
        factory.setMaxConcurrentConsumers(10);
        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        //用这个始终会报错
//        factory.setTransactionManager(new RabbitTransactionManager(connectionFactory));
        return factory;
    }

//    /**
//     * 创建路由
//     *
//     * @return
//     */
//    @Bean
//    public TopicExchange tranExchange() {
//        return new TopicExchange("tranExchange");
//    }
//
//    /**
//     * 创建队列
//     *
//     * @return
//     */
//    @Bean
//    public Queue QueueTran() {
//        return new Queue("tranQueue", true);
//    }
//
//    /**
//     * 绑定
//     *
//     * @param QueueTran
//     * @param tranExchange
//     * @return
//     */
//    @Bean
//    public Binding bindingTran(Queue QueueTran, TopicExchange tranExchange) {
//        return BindingBuilder.bind(QueueTran).to(tranExchange).with("routingKeyTran");
//    }

    @Resource
    private Queue QueueTran;

    @Bean
    public SimpleMessageListenerContainer messageContainer(ConnectionFactory connectionFactory) throws Exception {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(rabbitConnectionFactory());
        container.setQueues(QueueTran);
        container.setExposeListenerChannel(true);
        container.setMaxConcurrentConsumers(1);
        container.setConcurrentConsumers(1);
        container.setAcknowledgeMode(AcknowledgeMode.MANUAL); //设置确认模式手工确认
//        container.setTransactionManager(new RabbitTransactionManager(connectionFactory));
        container.setMessageListener(new ChannelAwareMessageListener() {
            @Override
            public void onMessage(Message message, Channel channel) throws Exception {
                byte[] body = message.getBody();
                System.out.println("receive msg b: " + new String(body));
//                Thread.sleep(30000);
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false); //确认消息成功消费

            }
        });
        return container;
    }

//    @Bean
//    public RabbitAdmin rabbitTemplate(ConnectionFactory connectionFactory) {
//        return new RabbitAdmin(connectionFactory);
//    }

//    @Bean
//    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter messageConverter) {
//        RabbitTemplate template = new RabbitTemplate(connectionFactory);
//        template.setMessageConverter(messageConverter);
//        return template;
//    }
//
//    @Bean
//    public MessageConverter messageConverter() {
//        return new ContentTypeDelegatingMessageConverter(new Jackson2JsonMessageConverter());
//    }
}