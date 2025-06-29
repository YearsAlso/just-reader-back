package com.yearsalso.data.constant;

public interface RabbitMqConst {
    String HANDLE_TOPIC_NAME = "das.justReader.handle";

    String COMPLETE_TOPIC_NAME = "das.justReader.complete";

    String COMPLETE_QUEUE_NAME = "das.justReader.complete";

    String COMPLETE_QUEUE_WAIT_HANDLE = "das.justReader.wait.handle";

    String CACHE_COMPLETE_QUEUE_NAME = "das.justReader.cache.complete";
}
