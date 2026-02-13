package com.yearsalso.client.listener;


import com.rabbitmq.client.impl.AMQImpl;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
@RabbitListener(queues = "just-reader-sse-push-queue")
public class SsePushListener {

    final String SSE_PUSH_QUEUE_NAME = "just-reader-sse-push-queue";

    // TODO：监听 MQ 中的 SSE 推送消息，并处理这些消息

    @RabbitHandler
    public void onRegistrationMessageFromMailQueue(Map message) throws Exception {
        log.info("queue {} received registration message: {}", SSE_PUSH_QUEUE_NAME, message);
    }
}
