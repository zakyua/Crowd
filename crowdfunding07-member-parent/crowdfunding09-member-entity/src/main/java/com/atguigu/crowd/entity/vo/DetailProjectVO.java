package com.atguigu.crowd.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 项目的细节信息
 * @author ChenCheng
 * @create 2022-06-07 15:30
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetailProjectVO {

    // 项目的id
    private Integer projectId;
    // 项目的名称
    private String projectName;
    // 项目的描述
    private String projectDesc;
    private Integer day;
    private Integer followerCount;
    // 项目当前的状态
    private Integer status;
    private String statusText;
    // 项目需要筹集的金额
    private Integer money;
    // 项目以筹集到的金额
    private Integer supportMoney;
    // 项目完成的百分比
    private Integer percentage;
    // 项目的发起时间
    private String deployDate;
    // 项目的最后日期
    private Integer lastDay;
    // 项目的支持人数
    private Integer supporterCount;
    // 项目的头图
    private String headerPicturePath;
    // 项目详情图片
    private List<String> detailPicturePathList;
    // 项目回报信息的集合
    private List<DetailReturnVO> detailReturnVOList;

}
