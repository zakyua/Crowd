package com.atguigu.crowd.service.api;

import com.atguigu.crowd.entity.Role;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @author ChenCheng
 * @create 2022-05-17 21:08
 */
public interface RoleService {


    PageInfo<Role> getPageInfo(Integer pageNum, String keyword, Integer pageSize);

    void saveRole(Role role);

    void updateRole(Role role);

    void removeRole(List<Integer> roleIdList);

    List<Role> getAssignedRole(Integer adminId);

    List<Role> getUnAssignedRole(Integer adminId);

    void saveAdminRoleRelationship(Integer adminId,List<Integer> roleIdList);
}
