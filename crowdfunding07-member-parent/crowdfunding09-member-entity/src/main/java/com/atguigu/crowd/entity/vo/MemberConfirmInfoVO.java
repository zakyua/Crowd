package com.atguigu.crowd.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**  页面上收集项目发起人的确认信息
 * @author ChenCheng
 * @create 2022-06-03 13:20
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberConfirmInfoVO {
    //	易付宝账号
    private String paynum;
    //	法人身份证号
    private String cardnum;
}
