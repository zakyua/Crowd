package com.atguigu.crowd.mvc.handler;

import com.atguigu.crowd.entity.Role;
import com.atguigu.crowd.mapper.RoleMapper;
import com.atguigu.crowd.service.api.RoleService;
import com.atguigu.crowd.util.ResultEntity;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author ChenCheng
 * @create 2022-05-17 21:03
 */
@Controller
public class RoleHandler {

    @Autowired
    private RoleService roleService;


    @ResponseBody
    @RequestMapping("/role/get/page/info.json")
    public ResultEntity<PageInfo<Role>> getPageInfo(
            @RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum,
            @RequestParam(value = "keyword",defaultValue = "") String keyword,
            @RequestParam(value = "pageSize",defaultValue = "5") Integer pageSize){
        //    根据关键字去数据库查询所有的角色的数据,分页显示数据
        PageInfo<Role> pageInfo = roleService.getPageInfo(pageNum,keyword,pageSize);
        return ResultEntity.successWithData(pageInfo);
    }


    /**
     *  添加角色
     * @param role
     * @return
     */
    @ResponseBody
    @RequestMapping("/role/save.json")
    public ResultEntity<String> saveRole(Role role){
        roleService.saveRole(role);
        return ResultEntity.successWithOutData();
    }


    //role/update.json

    /**
     * 更新角色
     * @param role
     */
    @ResponseBody
    @RequestMapping("/role/update.json")
    public ResultEntity<String> test(Role role){

        roleService.updateRole(role);

        return ResultEntity.successWithOutData();
    }

    /**
     *  删除角色数据
     */
    @ResponseBody
    @RequestMapping("/role/remove/by/role/id/array.json")
    public ResultEntity<String> removeRoleList(@RequestBody List<Integer> roleIdList ){
       roleService.removeRole(roleIdList);
       return ResultEntity.successWithOutData();
    }


}
