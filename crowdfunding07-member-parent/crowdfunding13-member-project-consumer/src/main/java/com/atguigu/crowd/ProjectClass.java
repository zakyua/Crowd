package com.atguigu.crowd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author ChenCheng
 * @create 2022-06-01 8:26
 */
@EnableFeignClients     // 该模块也需要调用远程的方法
@SpringBootApplication
public class ProjectClass {
    public static void main(String[] args) {
        SpringApplication.run(ProjectClass.class,args);
    }
}
