package com.atguigu.crowd.entity.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// 项目详情图片
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectItemPicPO {
    private Integer id;
    // 所属的项目
    private Integer projectid;
    // 图片的路径
    private String itemPicPath;

}