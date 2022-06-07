package com.atguigu.crowd.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author ChenCheng
 * @create 2022-06-02 18:35
 */
@Configuration
public class CrowdWebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {


        // 配置前往发起众筹的页面
        registry.addViewController("/agree/protocol/page.html").setViewName("project-agree");

        // 配置前往发起项目的页面
        registry.addViewController("/do/crowd/launch/page.html").setViewName("project-launch");

        // 配置前往汇报信息的页面
        registry.addViewController("/return/info/page.html").setViewName("project-return");

        // 配置前往确认信息的页面
        registry.addViewController("/create/confirm/page.html").setViewName("project-confirm");


        // 配置前往成功的页面
        registry.addViewController("/create/success.html").setViewName("project-success");






    }
}
