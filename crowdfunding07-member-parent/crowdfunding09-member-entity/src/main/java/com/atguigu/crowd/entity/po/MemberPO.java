package com.atguigu.crowd.entity.po;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// 用户表
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberPO {
    private Integer id;

    // 登录的账号
    private String loginacct;

    // 登录的密码
    private String userpswd;

    // 用户的姓名
    private String username;

    // 邮箱
    private String email;

    //
    private Integer authstatus;

    private Integer usertype;

    private String realname;

    private String cardnum;

    private Integer accttype;

}