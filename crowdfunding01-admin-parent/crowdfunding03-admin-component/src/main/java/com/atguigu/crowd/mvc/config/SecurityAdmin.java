package com.atguigu.crowd.mvc.config;

import com.atguigu.crowd.entity.Admin;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.List;

/**
 * @author ChenCheng
 * @create 2022-05-23 18:40
 */
public class SecurityAdmin extends User {

     static final long serialVersionUID = 56545456L;

    private Admin originalAdmin;

    public SecurityAdmin(Admin originalAdmin ,List<GrantedAuthority> authorities) {

        // 这里就将这个密码给使用完以后，就给他擦除，避免别人再次使用
        super(originalAdmin.getUserName(), originalAdmin.getUserPswd(), authorities);
        this.originalAdmin = originalAdmin;
        // 将原始Admin对象中的密码擦除
        this.originalAdmin.setUserPswd(null);

    }



    // 对外提供的获取原始Admin对象的getXxx()方法
    public Admin getOriginalAdmin() {
        return originalAdmin;
    }
}
