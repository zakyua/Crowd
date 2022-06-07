package com.atguigu;

import com.atguigu.crowd.RedisClass;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.ClusterOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.Calendar;

/**
 * @author ChenCheng
 * @create 2022-05-31 20:04
 */
@SpringBootTest(classes = RedisClass.class)
public class RedisTest {

    @Autowired
    private StringRedisTemplate redisTemplate;


    @Test
    public void testInsent(){
        ValueOperations<String, String> stringStringValueOperations =
                redisTemplate.opsForValue();
        stringStringValueOperations.set("fruit", "苹果");
    }





}
