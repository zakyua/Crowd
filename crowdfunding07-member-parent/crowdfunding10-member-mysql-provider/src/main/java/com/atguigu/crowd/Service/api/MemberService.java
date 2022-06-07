package com.atguigu.crowd.Service.api;

import com.atguigu.crowd.entity.po.MemberPO;
import org.springframework.stereotype.Service;

/**
 * @author ChenCheng
 * @create 2022-06-01 9:22
 */
public interface MemberService {

    // 根据用户查询用户
    MemberPO getMemberPOByLoginAcct(String loginAcct);


    void saveMemberRemote(MemberPO memberPO);
}
