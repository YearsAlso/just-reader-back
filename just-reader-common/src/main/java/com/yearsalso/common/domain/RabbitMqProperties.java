package com.yearsalso.common.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Data
/**
 * RabbitMQ配置属性类
 * 用于封装与RabbitMQ连接和队列相关的基础配置信息
 */
@Component
@ConfigurationProperties(prefix = "spring.rabbitmq")
public class RabbitMqProperties {
    /**
     * 存储消息的队列名称
     * 用于指定消息的接收端从哪个队列中消费消息
     */
    private String queueName;

    /**
     * 主题名称
     * 在发布/订阅模式中用于消息的分类和路由
     */
    private String topicName;

    /**
     * RabbitMQ登录用户名
     * 用于认证和连接RabbitMQ服务器
     */
    private String usrName;

    /**
     * RabbitMQ登录密码
     * 配合用户名进行RabbitMQ服务的身份验证
     */
    private String password;

    /**
     * RabbitMQ Broker地址
     * 格式为：协议://主机名或IP:端口号，例如 amqp://localhost:5672
     * 用于建立与消息中间件的连接
     */
    private String brokerUrl;

    /**
     * RabbitMQ主机地址
     */
    private String host = "localhost";

    /**
     * RabbitMQ端口
     */
    private int port = 5672;

    /**
     * 虚拟主机
     */
    private String virtualHost = "/";

    /**
     * 是否启用SSL
     */
    private boolean sslEnabled = false;
}