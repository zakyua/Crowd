package com.atguigu.crowd.exception;

/**
 * 对需要访问特殊的资源的异常 这个xml文件去配置
 * @author ChenCheng
 * @create 2022-05-12 20:32
 */
public class AccessForbiddenException extends RuntimeException{
    static final long serialVersionUID = 897190745766939L;



    public AccessForbiddenException() {
        super();
    }

    public AccessForbiddenException(String message) {
        super(message);
    }

    public AccessForbiddenException(String message, Throwable cause) {
        super(message, cause);
    }

    public AccessForbiddenException(Throwable cause) {
        super(cause);
    }

    protected AccessForbiddenException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
