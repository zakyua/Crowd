package com.atguigu.crowd.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**    页面上项目发起人的信息
 * @author ChenCheng
 * @create 2022-06-03 13:18
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberLauchInfoVO {
    //	简单介绍
    private String descriptionSimple;
    //	详细介绍
    private String descriptionDetail;
    //	联系电话
    private String phoneNum;
    //	客服电话
    private String serviceNum;
}
