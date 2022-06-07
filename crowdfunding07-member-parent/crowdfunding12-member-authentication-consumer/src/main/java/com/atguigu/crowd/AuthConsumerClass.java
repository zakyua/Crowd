package com.atguigu.crowd;

import com.atguigu.crowd.api.MySQLRemoteService;
import com.atguigu.crowd.api.RedisRemoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.util.AntPathMatcher;

/**
 * @author ChenCheng
 * @create 2022-05-31 21:50
 */
@EnableFeignClients          // 这个微服务是一个消费者，他需要远程访问别的微服务
@SpringBootApplication
public class AuthConsumerClass {
    public static void main(String[] args) {
        SpringApplication.run(AuthConsumerClass.class,args);
    }


}
