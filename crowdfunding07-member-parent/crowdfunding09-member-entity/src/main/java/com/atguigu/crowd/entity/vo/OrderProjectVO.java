package com.atguigu.crowd.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author ChenCheng
 * @create 2022-06-07 16:25
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderProjectVO implements Serializable {

    private static final long serialVersionUID = 1L;


    private Integer id;

    // 项目的名称
    private String projectName;

    // 项目的发起人
    private String launchName;
    // 回报的内容
    private String returnContent;

    // 回报的数量
    private Integer returnCount;
    private Integer supportPrice;

    private Integer freight;
    private Integer orderId;

    //	是否限制单笔购买数量，0 表示不限购，1 表示限购
    private Integer signalPurchase;

    //	如果单笔限购，那么具体的限购数量
    private Integer purchase;


}
