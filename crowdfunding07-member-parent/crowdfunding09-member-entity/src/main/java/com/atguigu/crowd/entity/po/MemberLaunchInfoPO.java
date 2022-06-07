package com.atguigu.crowd.entity.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// 项目发起人信息表
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberLaunchInfoPO {
    private Integer id;
    // 会员id
    private Integer memberid;
    // 简单介绍
    private String descriptionSimple;
    // 详细介绍
    private String descriptionDetail;
    // 联系电话
    private String phoneNum;
    // 客服电话
    private String serviceNum;

}