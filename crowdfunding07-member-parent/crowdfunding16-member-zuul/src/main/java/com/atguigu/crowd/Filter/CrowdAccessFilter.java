package com.atguigu.crowd.Filter;

import com.atguigu.crowd.constant.CrowdConstant;
import com.atguigu.crowd.util.AccessPassResources;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.ZuulFilterResult;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @author ChenCheng
 * @create 2022-06-02 17:21
 */
@Component
public class CrowdAccessFilter extends ZuulFilter {

    // return "pre" 表示在请求发生其前进行过滤
    @Override
    public String filterType() {
        return "pre";
    }

    // 决定那些资源需要放行，拦截后执行run  false表示放行
    @Override
    public boolean shouldFilter() {
        // 1.获取currentContext
        RequestContext currentContext = RequestContext.getCurrentContext();
        // 2.获取HttpServletRequest
        HttpServletRequest request = currentContext.getRequest();
        // 3.获取请求路径
        String servletPath = request.getServletPath();
        // 4.判断当前请求是否是需要放行的路径
        boolean isPassContains = AccessPassResources.PASS_RES_SET.contains(servletPath);
        if(isPassContains){
            // 是需要放行的路径,放行
            return false;
        }
        // 5.判断当前请求是否访问静态资源
        boolean isStaticResource = AccessPassResources.judgeIsStaticResource(servletPath);
        if(isStaticResource){
            // 访问静态资源,放行
            return false;
        }

        // 返回true,拦截该请求,然后去run方法中进行判断
        return true;
    }

    // 那些需要执行run决定放行以后去哪里
    @Override
    public Object run() throws ZuulException {
        // 1.获取currentContext
        RequestContext currentContext = RequestContext.getCurrentContext();
        // 2.获取HttpServletRequest
        HttpServletRequest request = currentContext.getRequest();
        // 3.获取session
        HttpSession session = request.getSession();
        // 3.从session中获取用户、
        // (这里能取出session这个值是因为我们在放行的登录资源中已经将这个值保存在作用域中了,使用了spring-session可以实现session共享)
        Object loginMember = session.getAttribute(CrowdConstant.ATTR_NAME_LOGIN_MEMBER);
        // 4.判断这个loginMember是否为空
        if(loginMember == null){
           // 这个loginMember如果为空表示当前用户没有登录,需要让他先登录才能访问被保护的资源
           // 因为我们需要在别的微服务中展示的页面显示错误信息，所以需要将信息保存在session中
        session.setAttribute(CrowdConstant.ATTR_NAME_MESSAGE,CrowdConstant.MESSAGE_ACCESS_FORBIDEN);
        // 5.获取HttpServletResponse对象
        HttpServletResponse response = currentContext.getResponse();
        // 6.重定向到登录的页面
            try {
                response.sendRedirect("/auth/to/member/login/page.html");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // 7.返回null就是不需要进行特殊处理
        return null;
    }

    @Override
    public int filterOrder() {
        return 0;
    }
}
