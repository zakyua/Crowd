package com.atguigu.crowd.handler;

import com.atguigu.crowd.Service.api.MemberService;
import com.atguigu.crowd.entity.po.MemberPO;
import com.atguigu.crowd.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ChenCheng
 * @create 2022-05-31 19:51
 */
@RestController
public class MemberProviderHandler {


    @Autowired
    private MemberService memberService;

    @RequestMapping("/get/member/by/login/acct/remote")
    public ResultEntity<MemberPO> getMemberPOByLoginAcctRemote(@RequestParam("loginAcct") String loginAcct){

        // 1.通过loginAcct去数据库将当前用户查询出来
        try {
            MemberPO memberPO = memberService.getMemberPOByLoginAcct(loginAcct);
       // 2.查询成功将消息返回
            return  ResultEntity.successWithData(memberPO);
        } catch (Exception e) {
            e.printStackTrace();
        // 3.查询失败将失败的信息返回
            return ResultEntity.failed(e.getMessage());
        }

    }


    /**
     *     添加一个用户
     * @param memberPO
     * @return
     */
    @RequestMapping("/save/member/remote")
    public ResultEntity<String> saveMemberRemote(@RequestBody MemberPO memberPO){

        try {
            memberService.saveMemberRemote(memberPO);
            return ResultEntity.successWithOutData();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }


    }




}
