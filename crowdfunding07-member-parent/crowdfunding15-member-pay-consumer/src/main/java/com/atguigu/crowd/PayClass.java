package com.atguigu.crowd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author ChenCheng
 * @create 2022-06-01 8:33
 */
@EnableFeignClients        // 该模块也需要远程调方法
@SpringBootApplication
public class PayClass {
    public static void main(String[] args) {
        SpringApplication.run(PayClass.class,args);
    }
}
