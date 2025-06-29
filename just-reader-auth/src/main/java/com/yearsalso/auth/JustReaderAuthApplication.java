package com.yearsalso.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = "com.yearsalso.auth", exclude = {DataSourceAutoConfiguration.class})
public class JustReaderAuthApplication {
    public static void main(String[] args) {
        SpringApplication.run(JustReaderAuthApplication.class, args);
    }

}
