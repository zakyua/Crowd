package com.atguigu.crowd.mvc.handler;

import com.atguigu.crowd.constant.CrowdConstant;
import com.atguigu.crowd.entity.Auth;
import com.atguigu.crowd.entity.Role;
import com.atguigu.crowd.service.api.AdminService;
import com.atguigu.crowd.service.api.AuthService;
import com.atguigu.crowd.service.api.RoleService;
import com.atguigu.crowd.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * @author ChenCheng
 * @create 2022-05-21 18:56
 */
@Controller
public class AssignHandler {
    @Autowired
    private RoleService roleService;
    @Autowired
    private AdminService adminService;
    @Autowired
    private AuthService authService;


    /**
     *          这里去把数据库里的当前的用户所拥护的角色和未拥有的角色
     * @param adminId
     * @param modelMap
     * @return
     */
    @RequestMapping("/assign/to/assign/role/page.html")
    public String toAdminAssignRolePage(@RequestParam("adminId") Integer adminId, ModelMap modelMap){


        // 1.查询已经已经拥有的角色
        List<Role> assignedRoleList = roleService.getAssignedRole(adminId);


         // 2.查询未拥有的角色
        List<Role> unAssignedRoleList = roleService.getUnAssignedRole(adminId);


        // 3.调转页面，带上我们准备好的数据
        modelMap.put(CrowdConstant.ATTR_NAME_ASSIGNED_ROLE,assignedRoleList);
        modelMap.put(CrowdConstant.ATTR_NAME_UNASSIGNED_ROLE,unAssignedRoleList);

        // 4.跳转页面

        return  "assign-role";
    }


    /**
     *     给用户分配角色
     * @param adminId
     * @param pageNum
     * @param keyword
     * @param roleIdList
     * @return
     */
    @RequestMapping("/assign/do/assign.html")
    public String saveAdminRoleRelationship(@RequestParam("adminId") Integer adminId,
                                            @RequestParam("pageNum") Integer pageNum,
                                            @RequestParam("keyword") String keyword,
                                            @RequestParam(value = "roleIdList",required = false) List<Integer> roleIdList
                                          ){
        // 根据这个 roleIdList来给给用户设置角色
        adminService.saveAdminRoleRelationship(adminId,roleIdList);
        // 最后是让他回到用户的主页面
        return "redirect:/admin/get/page.html?pageNum="+pageNum+"&keyword="+keyword;

    }


    /**
     * 去数据库查询出所有的权限信息
     */
    @ResponseBody
    @RequestMapping("/assgin/get/all/auth.json")
    public ResultEntity<List<Auth>> getAllAuth(){
        List<Auth> authList = authService.getAll();
        return ResultEntity.successWithData(authList);
    }


    /**
     *  查询当前角色所拥有的权限
     * @param roleId
     */
    @ResponseBody
    @RequestMapping("/assign/get/assigned/auth/id/by/role/id.json")
    public ResultEntity<List<Integer>> getAuthByRoleId(@RequestParam("roleId") Integer roleId){

      // 根据当前的角色的id去把他拥有的权限的id查询出来
      List<Integer> authIdList =  authService.getAuthByRoleId(roleId);

      return ResultEntity.successWithData(authIdList);

    }


    /**
     *  更新角色的权限信息
     * @param map
     * @return
     */
    @ResponseBody
    @RequestMapping("/assign/do/role/assign/auth.json")
    public ResultEntity<String> test(@RequestBody Map<String,List<Integer>> map ){
        authService.saveRoleAuthRelathinship(map);
        return ResultEntity.successWithOutData();
    }







}
