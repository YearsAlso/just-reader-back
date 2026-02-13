package com.yearsalso.client.config;

import com.yearsalso.common.domain.RabbitMqProperties;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * 通用RabbitMQ配置类
 * 提供完整的RabbitMQ配置，包括连接工厂、RabbitTemplate、队列、交换机等
 */
@Configuration
public class GenericRabbitMqConfig {

    @Autowired
    private RabbitMqProperties rabbitMqProperties;

    /**
     * 创建连接工厂
     *
     * @return ConnectionFactory实例
     */
    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setHost(rabbitMqProperties.getHost());
        connectionFactory.setPort(rabbitMqProperties.getPort());
        connectionFactory.setUsername(rabbitMqProperties.getUsrName());
        connectionFactory.setPassword(rabbitMqProperties.getPassword());
        connectionFactory.setVirtualHost(rabbitMqProperties.getVirtualHost());
        connectionFactory.setPublisherConfirmType(CachingConnectionFactory.ConfirmType.CORRELATED);
        connectionFactory.setPublisherReturns(true);
        return connectionFactory;
    }

    /**
     * 创建RabbitTemplate实例
     *
     * @param connectionFactory 连接工厂
     * @return RabbitTemplate实例
     */
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(createMessageConverter());
        rabbitTemplate.setMandatory(true);
        return rabbitTemplate;
    }

    /**
     * 创建消息转换器
     *
     * @return Jackson2JsonMessageConverter实例
     */
    @Bean
    public MessageConverter createMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    /**
     * 创建默认Direct交换机
     *
     * @return DirectExchange实例
     */
    @Bean
    public DirectExchange defaultDirectExchange() {
        return new DirectExchange("just-reader.direct.exchange", true, false);
    }

    /**
     * 创建默认Topic交换机
     *
     * @return TopicExchange实例
     */
    @Bean
    public TopicExchange defaultTopicExchange() {
        return new TopicExchange("just-reader.topic.exchange", true, false);
    }

    /**
     * 创建默认Fanout交换机
     *
     * @return FanoutExchange实例
     */
    @Bean
    public FanoutExchange defaultFanoutExchange() {
        return new FanoutExchange("just-reader.fanout.exchange", true, false);
    }

    /**
     * 创建默认队列
     *
     * @return Queue实例
     */
    @Bean
    public Queue defaultQueue() {
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-dead-letter-exchange", "just-reader.dlx.exchange");
        arguments.put("x-dead-letter-routing-key", "dlx.routing.key");
        return new Queue("just-reader.default.queue", true, false, false, arguments);
    }

    /**
     * 创建死信队列
     *
     * @return Queue实例
     */
    @Bean
    public Queue deadLetterQueue() {
        return new Queue("just-reader.dead.letter.queue", true);
    }

    /**
     * 创建默认绑定关系 - Direct交换机与默认队列
     *
     * @param defaultQueue        默认队列
     * @param defaultDirectExchange 默认Direct交换机
     * @return Binding实例
     */
    @Bean
    public Binding defaultDirectBinding(Queue defaultQueue, DirectExchange defaultDirectExchange) {
        return BindingBuilder.bind(defaultQueue).to(defaultDirectExchange).with("default.routing.key");
    }

    /**
     * 创建默认绑定关系 - Topic交换机与默认队列
     *
     * @param defaultQueue       默认队列
     * @param defaultTopicExchange 默认Topic交换机
     * @return Binding实例
     */
    @Bean
    public Binding defaultTopicBinding(Queue defaultQueue, TopicExchange defaultTopicExchange) {
        return BindingBuilder.bind(defaultQueue).to(defaultTopicExchange).with("default.#");
    }

    /**
     * 创建默认绑定关系 - Fanout交换机与默认队列
     *
     * @param defaultQueue         默认队列
     * @param defaultFanoutExchange 默认Fanout交换机
     * @return Binding实例
     */
    @Bean
    public Binding defaultFanoutBinding(Queue defaultQueue, FanoutExchange defaultFanoutExchange) {
        return BindingBuilder.bind(defaultQueue).to(defaultFanoutExchange);
    }

    /**
     * 创建死信队列绑定关系
     *
     * @param deadLetterQueue 死信队列
     * @return Binding实例
     */
    @Bean
    public Binding deadLetterBinding(Queue deadLetterQueue) {
        return BindingBuilder.bind(deadLetterQueue).to(new TopicExchange("just-reader.dlx.exchange")).with("dlx.#");
    }
}