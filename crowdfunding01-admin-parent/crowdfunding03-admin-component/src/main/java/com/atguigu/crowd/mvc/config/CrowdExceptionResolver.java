package com.atguigu.crowd.mvc.config;

import com.atguigu.crowd.exception.AccessForbiddenException;
import com.atguigu.crowd.exception.LoginAcctAlreadyInUseException;
import com.atguigu.crowd.exception.LoginAcctAlreadyInUseForUpdateException;
import com.atguigu.crowd.exception.LoginFailedException;
import com.atguigu.crowd.util.CrowdUtil;
import com.atguigu.crowd.util.ResultEntity;
import com.google.gson.Gson;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.interfaces.RSAKey;

/**
 * @ExceptionHandler 将具体的一个异常和方法对应起来
 * @ControllerAdvic 表明这是一个异常的处理类
 * @author ChenCheng
 * @create 2022-05-10 20:43
 */
@ControllerAdvice
public class CrowdExceptionResolver {


    /**
     *  处理登录的异常
     * @param exception
     * @return
     * @throws IOException
     */
    @ExceptionHandler(value = LoginFailedException.class)
    public ModelAndView loginFailedExceptionResolver(
            HttpServletRequest request ,
            HttpServletResponse response ,
            Exception exception) throws IOException {

        // 登录异常后需要返回到登录页面，然后在登录页面打印异常信息
        String viewName = "admin-login" ;
        return commonResolve(request, response, exception, viewName);

    }


    /**
     *    查询数据出现错误的异常
     * @param request
     * @param response
     * @param exception
     * @return
     * @throws IOException
     */
    @ExceptionHandler(value = LoginAcctAlreadyInUseException.class)
    public ModelAndView loginAcctAlreadyInUseException(
            HttpServletRequest request ,
            HttpServletResponse response ,
            Exception exception) throws IOException {

        // 登录异常后需要返回到登录页面，然后在登录页面打印异常信息
        String viewName = "admin-add" ;
        return commonResolve(request, response, exception, viewName);

    }

    /**
     * 修改数据出现的异常
     * @param request
     * @param response
     * @param exception
     * @return
     * @throws IOException
     */
    @ExceptionHandler(value = LoginAcctAlreadyInUseForUpdateException.class)
    public ModelAndView loginAcctAlreadyInUseForUpdateException(
            HttpServletRequest request ,
            HttpServletResponse response ,
            Exception exception) throws IOException {

        // 登录异常后需要返回到登录页面，然后在登录页面打印异常信息
        String viewName = "admin-edit" ;
        return commonResolve(request, response, exception, viewName);

    }


    /**
     *  处理访问被保护的资源的请求
     * @return
     * @throws IOException
     */
    @ExceptionHandler(value = AccessForbiddenException.class)
    public ModelAndView accessForbiddenException(
            HttpServletRequest request,
            HttpServletResponse response,
            Exception exception) throws IOException {

        // 访问保护的资源需要返回到主页面
        String viewName = "admin-login" ;
        return commonResolve(request, response, exception, viewName);
    }







    /**
     *  处理算数的的异常
     * @param exception
     * @return
     * @throws IOException
     */
    @ExceptionHandler(value = ArithmeticException.class)
    public ModelAndView arithmeticExceptionResolver(
            HttpServletRequest request ,
            HttpServletResponse response ,
            Exception exception) throws IOException {

        // 设置异常出现或需要去的页面
        String viewName = "system-error" ;
        return commonResolve(request, response, exception, viewName);

    }


    /**
     *  处理空指针的异常
     * @return
     * @throws IOException
     */
    @ExceptionHandler(value = NullPointerException.class)
    public ModelAndView nullPointerExceptionResolver(
            HttpServletRequest request ,
            HttpServletResponse response ,
            Exception exception) throws IOException {

        // 设置异常出现或需要去的页面
        String viewName = "system-error" ;
        return commonResolve(request, response, exception, viewName);

    }


    /**
     *  处理异常机制的公共方法
     * @param viewName  普通请求,出现异常需要调转的页面
     * @return
     * @throws IOException
     */
    private ModelAndView commonResolve(
            HttpServletRequest request ,
            HttpServletResponse response ,
            Exception exception,
            String viewName) throws IOException {


        // 1.获取当前请求的类型
        boolean judgeResult = CrowdUtil.judgeRequestType(request);

        // 2.判断当前请求是否为一个ajax请求  ajax请求的异常处理机制返回json数据作为响应体
        if(judgeResult){
            // 3.创建Ajax请求返回的结果的对象
            ResultEntity<Object> failed = ResultEntity.failed(exception.getMessage());

            // 然后这里没有经过@ResponseBody来帮助我们处理java对象，我们需要将java对象转换为json对象
            // 4.获取Gson对象
            Gson gson = new Gson();
            // 5.将ResultEntity对象转换为json字符串
            String json = gson.toJson(failed);
            // 6.将json字符串作为响应体响应
            response.getWriter().write(json);
            // 7.不需要返回视图
            return null;
        }

        // 8.如果这是一个普通的请求，创建ModelAndView对象
        ModelAndView modelAndView = new ModelAndView();
        // 9.将exception保存到modelAndView
        modelAndView.addObject("exception",exception);
        // 10.设置对应的名称
        modelAndView.setViewName(viewName);

        return modelAndView;



    }





}
