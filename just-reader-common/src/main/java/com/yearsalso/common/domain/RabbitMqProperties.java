package com.yearsalso.common.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Data
public class RabbitMqProperties {
    private String queueName;

    private String topicName;

    private String usrName;

    private  String password;

    private  String brokerUrl;
}
