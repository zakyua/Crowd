package com.atguigu.crowd.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**   登陆首页当中需要展示的项目的分类信息
 * @author ChenCheng
 * @create 2022-06-03 21:12
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PortalTypeVO {
    private Integer id;
    // 分类信息的名称
    private String name;
    // 分类信息的备注
    private String remark;
    // 项目的集合
    private List<PortalProjectVO> portalProjectVOList;
}

