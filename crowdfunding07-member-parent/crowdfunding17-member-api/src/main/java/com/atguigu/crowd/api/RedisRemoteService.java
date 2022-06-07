package com.atguigu.crowd.api;

import com.atguigu.crowd.util.ResultEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.concurrent.TimeUnit;

/**
 * @author ChenCheng
 * @create 2022-05-31 21:48
 */
@FeignClient("crowd-redis") // 于远程的微服务crowd-redis建立连接
public interface RedisRemoteService {


    // 保存验证码的请求
    @RequestMapping("/set/redis/key/value/with/timeout/remote")
    public ResultEntity setRedisKeyValueWithTimeoutRemote(
            @RequestParam("key") String key,
            @RequestParam("value") String value,
            @RequestParam("time") long time,
            @RequestParam("timeUnit") TimeUnit timeUnit
    );


    // 查询验证码的请求
    @RequestMapping("/get/redis/value/by/key/remote")
    public ResultEntity<String> getRedisValueByKeyRemote(@RequestParam("key") String key);



    // 删除验证码的请求
    @RequestMapping("/remove/redis/key/by/key/remote")
    public ResultEntity<String> removeRedisKeyByKeyRemote(@RequestParam("key") String key);

}
