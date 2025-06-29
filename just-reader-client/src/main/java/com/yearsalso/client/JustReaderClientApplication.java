package com.yearsalso.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = "com.yearsalso.client", exclude = {DataSourceAutoConfiguration.class})
public class JustReaderClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(JustReaderClientApplication.class, args);
    }
}
