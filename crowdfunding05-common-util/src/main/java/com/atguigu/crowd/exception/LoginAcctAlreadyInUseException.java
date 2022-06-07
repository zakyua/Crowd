package com.atguigu.crowd.exception;

/**
 *  插入数据出现错误的异常
 * @author ChenCheng
 * @create 2022-05-17 16:47
 */
public class LoginAcctAlreadyInUseException extends RuntimeException{
    static final long serialVersionUID = 4897190745766939L;


    public LoginAcctAlreadyInUseException() {
        super();
    }

    public LoginAcctAlreadyInUseException(String message) {
        super(message);
    }

    public LoginAcctAlreadyInUseException(String message, Throwable cause) {
        super(message, cause);
    }

    public LoginAcctAlreadyInUseException(Throwable cause) {
        super(cause);
    }

    protected LoginAcctAlreadyInUseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
