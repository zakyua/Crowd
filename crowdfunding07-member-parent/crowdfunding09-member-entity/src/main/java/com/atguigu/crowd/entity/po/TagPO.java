package com.atguigu.crowd.entity.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// 标签表
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TagPO {
    private Integer id;
    private Integer pid;
    // 标签的名称
    private String name;
}