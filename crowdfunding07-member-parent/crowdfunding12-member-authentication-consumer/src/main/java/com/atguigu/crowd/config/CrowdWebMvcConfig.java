package com.atguigu.crowd.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author ChenCheng
 * @create 2022-06-01 9:02
 */
@Configuration
public class CrowdWebMvcConfig implements WebMvcConfigurer {

    // 配置视图解析
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {

        // 配置去登录的页面
        registry.addViewController("/auth/to/member/login/page.html").setViewName("member-login");

        // 配置登录成功要去的主页面
        registry.addViewController("/auth/to/member/center/page.html").setViewName("member-center");

        // 配置用戶注册要去的页面
        registry.addViewController("/auth/to/member/reg/page.html").setViewName("member-reg");

        // 配置调转"我的众筹"页面
        registry.addViewController("/auth/to/member/crowd/page.html").setViewName("member-crowd");


    }
}
