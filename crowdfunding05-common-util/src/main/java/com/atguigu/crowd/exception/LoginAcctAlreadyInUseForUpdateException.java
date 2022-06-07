package com.atguigu.crowd.exception;

/**
 *
 * 保存和更新出现错误则抛出这个异常
 * @author ChenCheng
 * @create 2022-05-17 18:52
 */
public class LoginAcctAlreadyInUseForUpdateException extends RuntimeException {

    static final long serialVersionUID = 90745766939L;

    public LoginAcctAlreadyInUseForUpdateException() {
        super();
    }

    public LoginAcctAlreadyInUseForUpdateException(String message) {
        super(message);
    }

    public LoginAcctAlreadyInUseForUpdateException(String message, Throwable cause) {
        super(message, cause);
    }

    public LoginAcctAlreadyInUseForUpdateException(Throwable cause) {
        super(cause);
    }

    protected LoginAcctAlreadyInUseForUpdateException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
