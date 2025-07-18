package com.yearsalso.file;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
@EnableCaching
@MapperScan(value = {"com.yearsalso.**.mapper"})
@ComponentScan("com.yearsalso.**")
public class SkeyeproFileApplication {

    public static void main(String[] args) {
        SpringApplication.run(SkeyeproFileApplication.class, args);
    }

}
