package com.atguigu.crowd.Handler;

import com.atguigu.crowd.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * @author ChenCheng
 * @create 2022-06-02 10:52
 */
@RestController
public class RedisProviderHandler {

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     *    将验证码保存到redis中
     * @param key     保存到数据库中的键
     * @param value   保存到数据库中的值
     * @param time
     * @param timeUnit
     * @return
     *        保存成功返回状态码
     *        保存失败返回错误信息
     */
    @RequestMapping("/set/redis/key/value/with/timeout/remote")
    public ResultEntity setRedisKeyValueWithTimeoutRemote(
            @RequestParam("key") String key,
            @RequestParam("value") String value,
            @RequestParam("time") long time,
            @RequestParam("timeUnit") TimeUnit timeUnit){

        try {
            redisTemplate.opsForValue().set(key,value,time,timeUnit);
            return ResultEntity.successWithOutData();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }

    }


    /**
     *    根据key在redis中查询出value
     * @param key
     * @return
     */
    @RequestMapping("/get/redis/value/by/key/remote")
    public ResultEntity<String> getRedisValueByKeyRemote(@RequestParam("key") String key){

        try {
            String code = redisTemplate.opsForValue().get(key);

//            code.substring()
            String[] split = code.split(".");
            String newCode = split[1];
            return ResultEntity.successWithData(code);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }

    }


    /**
     *     删除redis中的验证码数据
     * @param key   需要删除的数据的key
     * @return
     */
    @RequestMapping("/remove/redis/key/by/key/remote")
    public ResultEntity<String> removeRedisKeyByKeyRemote(@RequestParam("key") String key){

        try {
            redisTemplate.delete(key);
            return ResultEntity.successWithOutData();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }


    }


}
