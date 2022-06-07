package com.atguigu.crowd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @author ChenCheng
 * @create 2022-05-31 18:06
 */
@EnableEurekaServer
@SpringBootApplication
public class EurekaMainClass {
    public static void main(String[] args) {
        SpringApplication.run(EurekaMainClass.class,args);
    }
}
