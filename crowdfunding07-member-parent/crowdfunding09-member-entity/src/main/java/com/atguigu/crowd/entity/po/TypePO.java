package com.atguigu.crowd.entity.po;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// 分类表
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TypePO {
    private Integer id;
    // 分类的名称
    private String name;
    // 分类的介绍
    private String remark;


}