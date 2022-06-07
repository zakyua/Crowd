package com.atguigu.crowd.util;

/**
 * 统一整个项目中Ajax请求返回的结果（未来也可以用于分布式架构各个模块间调用时返回统一类型）
 * @author ChenCheng
 * @create 2022-05-10 18:35
 */
public class ResultEntity<T> {


    public static final  String SUCCESS = "SUCCESS";   // 成功
    public static final  String FAILED =  "FAILED";   // 失败

    // 封装请求的结果(成功还是失败)
    private String  result;

    // 封装请求失败返回的信息
    private String message;

    // 封装请求返回的数据(这里带上泛型就可以确定要返回的信息式什么类型)
    private T Data;


    /**
     *
     * @param message 请求失败后返回的信息
     * @return
     */
    public static ResultEntity failed(String message){
        return  new ResultEntity(FAILED,message,null);
    }




    /**
     * 请求成功且不需要返回信息的工具方法
     * @return
     */
    public static <Type> ResultEntity<Type> successWithOutData(){

        return new ResultEntity<Type>(SUCCESS,null,null);
    }


    public static String getSUCCESS() {
        return SUCCESS;
    }

    public static String getFAILED() {
        return FAILED;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return Data;
    }

    public void setData(T data) {
        Data = data;
    }

    /**
     * 请求成功且需要返回信息的工具方法
     * @param data 请求返回的数据
     * @param <Type>
     * @return
     */
    public static <Type> ResultEntity<Type> successWithData(Type data){
        return new ResultEntity<Type>(SUCCESS,null,data);
    }


    @Override
    public String toString() {
        return "ResultEntity{" +
                "result='" + result + '\'' +
                ", message='" + message + '\'' +
                ", Data=" + Data +
                '}';
    }

    public ResultEntity(String result, String message, T data) {
        this.result = result;
        this.message = message;
        Data = data;
    }

    public ResultEntity() {
    }



}
