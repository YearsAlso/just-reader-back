package com.yearsalso.data.constant;

/**
 * RabbitMQ 常量定义接口
 * <p>
 * 该接口定义了系统中使用的 RabbitMQ 队列、交换机和路由键等常量。
 * </p>
 */
public interface RabbitMqConst {
    /**
     * 处理消息的主题名称
     */
    String HANDLE_TOPIC_NAME = "das.justReader.handle";

    /**
     * 完成消息的主题名称
     */
    String COMPLETE_TOPIC_NAME = "das.justReader.complete";

    /**
     * 完成消息的队列名称
     */
    String COMPLETE_QUEUE_NAME = "das.justReader.complete";

    /**
     * 等待处理消息的队列名称
     */
    String COMPLETE_QUEUE_WAIT_HANDLE = "das.justReader.wait.handle";

    /**
     * 缓存完成消息的队列名称
     */
    String CACHE_COMPLETE_QUEUE_NAME = "das.justReader.cache.complete";
}
