package com.atguigu.crowd.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author ChenCheng
 * @create 2022-06-01 10:03
 */
@Data
@AllArgsConstructor
@NoArgsConstructor                    // 将来在网络展示的就是这个实体类的信息
public class LoginMemberVO implements Serializable {
    private Integer id;
    private String userName;
    private String email;
}
