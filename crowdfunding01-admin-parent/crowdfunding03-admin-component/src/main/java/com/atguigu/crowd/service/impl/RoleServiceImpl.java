package com.atguigu.crowd.service.impl;

import com.atguigu.crowd.entity.Role;
import com.atguigu.crowd.entity.RoleExample;
import com.atguigu.crowd.mapper.RoleMapper;
import com.atguigu.crowd.service.api.RoleService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ChenCheng
 * @create 2022-05-17 21:09
 */
@Service
public class RoleServiceImpl implements RoleService {


    @Autowired
    private RoleMapper roleMapper;

    /**
     *  根据关键字去数据库查询所有的角色的数据,分页显示数据
     * @param pageNum
     * @param keyword
     * @param pageSize
     * @return
     */
    @Override
    public PageInfo<Role> getPageInfo(Integer pageNum, String keyword, Integer pageSize) {

        // 开启分页的功能
        PageHelper.startPage(pageNum, pageSize);

        // 查询角色信息
        List<Role> roleList = roleMapper.selectRoleByKeyword(keyword);

        return new PageInfo<Role>(roleList);
    }

    /**
     *  新增一个角色
     * @param role
     */
    @Override
    public void saveRole(Role role) {
        roleMapper.insert(role);
    }

    /**
     * 更新用户
     * @param role
     */
    @Override
    public void updateRole(Role role) {
        roleMapper.updateByPrimaryKey(role);
    }


    /**
     * 删除用户组
     * @param roleIdList
     */
    @Override
    public void removeRole(List<Integer> roleIdList) {

        RoleExample roleExample = new RoleExample();
        RoleExample.Criteria criteria = roleExample.createCriteria();
        criteria.andIdIn(roleIdList);

        roleMapper.deleteByExample(roleExample);
    }


    /**
     * 去数据库查询当前用户已经拥有的角色
      * @param adminId
     * @return
     */
    @Override
    public List<Role> getAssignedRole(Integer adminId) {
        return roleMapper.selectAssignedRole(adminId);
    }

    /**
     * 去数据库查询当前用户没有拥有的角色
     * @param adminId
     * @return
     */
    @Override
    public List<Role> getUnAssignedRole(Integer adminId) {
        return roleMapper.selectUnAssignedRole(adminId);
    }


    /**
     *     根据这个roleIdList来给这个当前的用户设置角色
     */
    @Override
    public void saveAdminRoleRelationship(Integer adminId, List<Integer> roleIdList) {


        // 1.先将当前用户的角色一次全部删除



        // 2.给当前用户设置角色




    }




}
