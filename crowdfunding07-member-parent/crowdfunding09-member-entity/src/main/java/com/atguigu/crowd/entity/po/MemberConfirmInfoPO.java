package com.atguigu.crowd.entity.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
// 发起人确认信息表
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberConfirmInfoPO {
    private Integer id;
    // 会员 id
    private Integer memberid;
    // 易付宝企业账号
    private String paynum;
    // 法人身份证号
    private String cardnum;

}