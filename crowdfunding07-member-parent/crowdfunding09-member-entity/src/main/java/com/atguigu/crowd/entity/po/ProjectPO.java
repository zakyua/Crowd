package com.atguigu.crowd.entity.po;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// 项目表
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectPO {
    private Integer id;
    // 项目的名称
    private String projectName;
    // 项目描述
    private String projectDescription;
    // 筹集金额
    private Long money;
    // 筹集天数
    private Integer day;
    // 项目当前的状态  '0-即将开始，1-众筹中，2-众筹成功，3-众筹失败
    private Integer status;
    // 项目发起时间
    private String deploydate;
    // 已筹集到的金额
    private Long supportmoney;
    // 支持人数
    private Integer supporter;
    // 百分比完成度
    private Integer completion;
    // 发起人的会员id
    private Integer memberid;
    // 项目创建时间
    private String createdate;
    // 关注人数
    private Integer follower;
    // 头图路径
    private String headerPicturePath;
}