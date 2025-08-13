package com.yearsalso.client.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 通用RabbitMQ配置类
 * 提供基础的消息队列配置，包括队列、交换机、绑定关系和消息转换器
 */
@Configuration
public class RabbitMqConfig {

    /**
     * 创建默认队列
     * @return Queue实例
     */
    @Bean
    public Queue defaultQueue() {
        return new Queue("just-reader.default.queue", true);
    }

    /**
     * 创建默认交换机
     * @return DirectExchange实例
     */
    @Bean
    public DirectExchange defaultExchange() {
        return new DirectExchange("just-reader.default.exchange");
    }

    /**
     * 创建默认绑定关系
     * @param defaultQueue 默认队列
     * @param defaultExchange 默认交换机
     * @return Binding实例
     */
    @Bean
    public Binding defaultBinding(Queue defaultQueue, DirectExchange defaultExchange) {
        return BindingBuilder.bind(defaultQueue).to(defaultExchange).with("default.routing.key");
    }

    /**
     * 创建RabbitTemplate实例
     * @param connectionFactory 连接工厂
     * @return RabbitTemplate实例
     */
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(createMessageConverter());
        return rabbitTemplate;
    }

    /**
     * 创建消息转换器
     * @return Jackson2JsonMessageConverter实例
     */
    @Bean
    public MessageConverter createMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}