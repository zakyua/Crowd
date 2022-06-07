package com.atguigu.crowd.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author ChenCheng
 * @create 2022-06-02 10:35
 */
@Component
@ConfigurationProperties(prefix = "short.message")
public class ShortMessageProperties {
    private String host;
    private String path;
    private String appCode;
    private String method;

    public ShortMessageProperties(String host, String path, String appCode, String method) {
        this.host = host;
        this.path = path;
        this.appCode = appCode;
        this.method = method;
    }

    public ShortMessageProperties() {
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setAppCode(String appCode) {
        this.appCode = appCode;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getHost() {
        return host;
    }

    public String getPath() {
        return path;
    }

    public String getAppCode() {
        return appCode;
    }

    public String getMethod() {
        return method;
    }
}
