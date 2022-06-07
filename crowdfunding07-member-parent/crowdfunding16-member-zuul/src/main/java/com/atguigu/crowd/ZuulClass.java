package com.atguigu.crowd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 * @author ChenCheng
 * @create 2022-06-01 8:42
 */
@EnableZuulProxy
@SpringBootApplication
public class ZuulClass {
    public static void main(String[] args) {
        SpringApplication.run(ZuulClass.class,args);
    }
}
