package com.atguigu.crowd.exception;

/**
 * 登录异常 这个类使用注解的异常处理机制
 * @author ChenCheng
 * @create 2022-05-11 18:37
 */
public class LoginFailedException  extends RuntimeException{

    static final long serialVersionUID = 5766939L;
    public LoginFailedException() {
        super();
    }

    public LoginFailedException(String message) {
        super(message);
    }

    public LoginFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    public LoginFailedException(Throwable cause) {
        super(cause);
    }

    protected LoginFailedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
