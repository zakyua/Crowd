package com.atguigu.crowd.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**     在登录首页展示的项目详情
 * @author ChenCheng
 * @create 2022-06-03 21:13
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PortalProjectVO {
    // 项目的id
    private Integer projectId;
    // 项目的名称
    private String projectName;
    // 项目的头图
    private String headerPicturePath;
    // 项目需要收集的金额
    private Integer money;
    // 项目创建的时间
    private String deployDate;
    // 百分比
    private Integer percentage;
    // 支持者
    private Integer supporter;
}
