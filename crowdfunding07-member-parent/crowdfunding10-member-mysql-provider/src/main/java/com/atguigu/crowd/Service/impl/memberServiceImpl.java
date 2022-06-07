package com.atguigu.crowd.Service.impl;

import com.atguigu.crowd.Service.api.MemberService;
import com.atguigu.crowd.entity.po.MemberPO;
import com.atguigu.crowd.entity.po.MemberPOExample;
import com.atguigu.crowd.mapper.MemberPOMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ChenCheng
 * @create 2022-06-01 9:23
 */
@Service
public class memberServiceImpl implements MemberService {

    @Autowired(required = false)
    private MemberPOMapper memberPOMapper;


    /**
     *  添加一个用户
     * @param memberPO
     */
    @Override
    public void saveMemberRemote(MemberPO memberPO) {
        memberPOMapper.insertSelective(memberPO);
    }

    /**
     *    根据用户名查询出当前用户
     * @param loginAcct
     * @return
     */
    @Override
    public MemberPO getMemberPOByLoginAcct(String loginAcct) {


        MemberPOExample memberPOExample = new MemberPOExample();
        MemberPOExample.Criteria criteria = memberPOExample.createCriteria();
        criteria.andLoginacctEqualTo(loginAcct);

        List<MemberPO> memberPOList = memberPOMapper.selectByExample(memberPOExample);

        // 判断得到的List是否为空，为空则返回null，防止后面调用的时候触发空指针异常
        if(memberPOList.size() == 0 && memberPOList == null){
            return null;
        }

        // List非空，则返回第一个（因为LoginAcct是唯一的）
        return  memberPOList.get(0);
    }
}
