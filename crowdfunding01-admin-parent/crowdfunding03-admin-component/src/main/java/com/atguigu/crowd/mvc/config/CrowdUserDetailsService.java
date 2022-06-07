package com.atguigu.crowd.mvc.config;

import com.atguigu.crowd.entity.Admin;
import com.atguigu.crowd.entity.Role;
import com.atguigu.crowd.service.api.AdminService;
import com.atguigu.crowd.service.api.AuthService;
import com.atguigu.crowd.service.api.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ChenCheng
 * @create 2022-05-23 17:33
 */
@Component
public class CrowdUserDetailsService implements UserDetailsService {

    @Autowired
    private AdminService adminService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private AuthService authService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // 1.根据 username来查询到这个admin4
        Admin admin = adminService.getAdminByLoginAcct(username);

        // 2.根据这个admin查询出他多用有的角色和权限
        Integer adminId = admin.getId();

            // 3.查询用户拥有的角色
        List<Role> assignedRoleList = roleService.getAssignedRole(adminId);

        // 4.查询角色拥有的权限
        List<String>  authNameList =  authService.getAssignedAuthNameByAdminId(adminId);

        // 5.创建集合对象用来存储GrantedAuthority
        List<GrantedAuthority> authorities = new ArrayList<>();

        // 6.遍历assignedRoleList存入角色信息
        for (Role role:
                assignedRoleList
             ) {

            // 添加角色的时候需要来设置 "ROLE_"
           String roleName  = "ROLE_" + role.getName();
           authorities.add(new SimpleGrantedAuthority(roleName));
        }


        // 7.遍历authNameList用来村塾权限的信息
        for (String authName:
                authNameList
             ) {
            authorities.add(new SimpleGrantedAuthority(authName));
        }
        SecurityAdmin securityAdmin = new SecurityAdmin(admin, authorities);
        return securityAdmin;
    }
}
