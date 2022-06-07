package com.atguigu.crowd.service.impl;

import com.atguigu.crowd.entity.Auth;
import com.atguigu.crowd.entity.AuthExample;
import com.atguigu.crowd.mapper.AuthMapper;
import com.atguigu.crowd.service.api.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author ChenCheng
 * @create 2022-05-22 11:13
 */
@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private AuthMapper authMapper;

    /**
     * 查询所有的权限信息
     * @return
     */
    @Override
    public List<Auth> getAll() {
        return authMapper.selectByExample(new AuthExample());
    }

    /**
     * 查询指定角色的权限信息
     * @param roleId
     * @return
     */
    @Override
    public List<Integer> getAuthByRoleId(Integer roleId) {
        return authMapper.selectAuthByRoleId(roleId);
    }


    /**
     * 更新角色的权限信息
     * @param map
     */
    @Override
    public void saveRoleAuthRelathinship(Map<String, List<Integer>> map) {

        // 将map中的角色信息取出
        List<Integer> roleIdList = map.get("roleId");
        Integer roleId = roleIdList.get(0);


        // 1. 将这个roleId之前拥有的权限信息全部删除
        authMapper.deleteOldAuth(roleId);


        // 2. 添加新的权限信息
        // 取出需要添加的权限
        List<Integer> authIdArray = map.get("authIdArray");
        if(authIdArray != null && authIdArray.size() >0){
            authMapper.insertNewRelationship(roleId,authIdArray);
        }

    }


    /**
     *  根据用户的id去把他具有的权限查询出来
     * @param adminId
     * @return
     */
    @Override
    public List<String> getAssignedAuthNameByAdminId(Integer adminId) {
        return authMapper.selectAssignedAuthNameByAdminId(adminId);
    }


    /**
     * 根据角色的集合查询权限的名称
     * @param roleIdList
     * @return
     */
    @Override
    public List<String> getAuthByRoleIdList(ArrayList<Integer> roleIdList) {
        return null;
    }


}
