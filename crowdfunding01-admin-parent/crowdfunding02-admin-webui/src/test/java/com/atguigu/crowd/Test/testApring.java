package com.atguigu.crowd.Test;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author ChenCheng
 * @create 2022-05-23 20:17
 */
public class testApring {


    public static void main(String[] args) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String encode = bCryptPasswordEncoder.encode("123123");
        System.out.println(encode);
    }
}
