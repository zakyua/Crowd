package com.atguigu.crowd.util;

import com.atguigu.crowd.constant.CrowdConstant;

import java.util.HashSet;
import java.util.Set;

/**   准备zuul需要放行那些资源
 * @author ChenCheng
 * @create 2022-06-02 17:24
 */
public class AccessPassResources {


    // 需要放行的请求
    // 保存不被过滤的请求
    public static final Set<String> PASS_RES_SET = new HashSet<>();

    // 静态代码块中加入不被过滤的内容
    static {
        // 去主页面的请求
        PASS_RES_SET.add("/");
        // 去注册页面的请求
        PASS_RES_SET.add("/auth/to/member/reg/page.html");
        // 去登录的页面的请求
        PASS_RES_SET.add("/auth/to/member/login/page.html");
        // 发送验证码的请求
        PASS_RES_SET.add("/auth/member/send/short/message.json");
        // 注册的请求
        PASS_RES_SET.add("/auth/member/do/register.html");
        // 登录的请求
        PASS_RES_SET.add("/auth/do/member/login.html");
        // 退出的请求
        PASS_RES_SET.add("/auth/do/member/logout.html");
        //
        PASS_RES_SET.add("/error");
        //
        PASS_RES_SET.add("/favicon.ico");
    }


    // 保存不被过滤的静态资源
    public static final Set<String> STATIC_RES_SET = new HashSet<>();

    // 静态代码块中加入不被过滤的内容
    static {
        STATIC_RES_SET.add("bootstrap");
        STATIC_RES_SET.add("css");
        STATIC_RES_SET.add("fonts");
        STATIC_RES_SET.add("img");
        STATIC_RES_SET.add("jquery");
        STATIC_RES_SET.add("layer");
        STATIC_RES_SET.add("script");
        STATIC_RES_SET.add("ztree");
    }


    /**
     *     判断当前的路径是否是访问静态资源的路径
     * @param servletPath
     * @return
     */
    public static boolean judgeIsStaticResource(String servletPath){

        // 1.判断当前路径是否有效
        if(servletPath == null || servletPath.length() ==0){
            throw new RuntimeException(CrowdConstant.MESSAGE_STRING_INVALIDATE);
        }

        // 2.通过"/"来分割得到的请求路径
        String[] split = servletPath.split("/");
        // 3.取出路径中的第一层     /css/***  取出这个css
        String path = split[1];

        return STATIC_RES_SET.contains(path);

    }




}
