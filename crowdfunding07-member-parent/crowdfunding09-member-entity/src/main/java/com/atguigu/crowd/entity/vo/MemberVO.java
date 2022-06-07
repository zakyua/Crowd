package com.atguigu.crowd.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ChenCheng
 * @create 2022-06-02 11:39
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberVO {
    private String loginAcct;
    private String userPswd;
    private String userName;
    private String email;
    private String phoneNum;
    private String code;
}
