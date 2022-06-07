package com.atguigu.crowd.mvc.handler;

import com.atguigu.crowd.constant.CrowdConstant;
import com.atguigu.crowd.entity.Admin;
import com.atguigu.crowd.exception.LoginFailedException;
import com.atguigu.crowd.service.api.AdminService;
import com.atguigu.crowd.service.impl.AdminServiceImpl;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

/**
 * @author ChenCheng
 * @create 2022-05-11 18:46
 */
@Controller
public class AdminHandler {

    @Autowired
    private AdminService adminService;

    private Logger logger = LoggerFactory.getLogger(AdminServiceImpl.class);


    // 修改用户
    @RequestMapping("/admin/update.html")
    public String update(Admin admin,
                         @RequestParam("pageNum") Integer pageNum,
                         @RequestParam("keyword") String keyword){

    adminService.updateAdmin(admin);

    // 跳转到用户列表的页面
    return "redirect:/admin/get/page.html?pageNum="+pageNum+"&keyword"+keyword;
    }


    // 修改用户的请求 admin/to/edit/page.html
    @RequestMapping("/admin/to/edit/page.html")
    public String toEditPage(@RequestParam("adminId") Integer adminId,Model model){

        Admin admin = adminService.getAdminById(adminId);
        model.addAttribute("admin",admin);
        // 跳转到修改用户的页面
        return "admin-edit";
    }


    // 新增用户
    @RequestMapping("/admin/save.html")
    public String save(Admin admin){
        adminService.saveAdmin(admin);
        // 重定向到分页查询管理员用户信息,去最后一页
        return "redirect:/admin/get/page.html?pageNum="+Integer.MAX_VALUE;
    }



    // 单条删除
    @RequestMapping("/admin/remove/{adminId}/{pageNum}/{keyword}.html")
    public String  remove(
            @PathVariable("adminId") Integer adminId,
            @PathVariable("pageNum") Integer pageNum,
            @PathVariable("keyword") String keyword){

        // 根据id单条删除
        adminService.remove(adminId);

        // 跳转到用户列表的页面
        return "redirect:/admin/get/page.html?pageNum="+pageNum+"&keyword="+keyword;

    }



    // 分页查询管理员用户信息
    @RequestMapping("/admin/get/page.html")
    public String getAdminPage(
                     @RequestParam(value = "keyword",defaultValue = "") String keyword,
                     @RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum,
                     @RequestParam(value = "pageSize",defaultValue = "5") Integer pageSize,
                     Model model){

        // 1.分页查询
        PageInfo<Admin> pageInfo = adminService.getPageInfo(keyword, pageNum, pageSize);



        // 2.将管理员信息共享到域对象中
        model.addAttribute(CrowdConstant.ATTR_NAME_PAGE_INFO,pageInfo);


        return "admin-page";
    }

    // 处理退出的请求
    @RequestMapping("/admin/do/logout.html")
    public String doLogout(HttpSession session){
        // 1.强制session失效
        session.invalidate();

        // 2.跳转到登录页面
        return "redirect:/admin/to/login/page.html";
    }




    // 处理登录请求
    @RequestMapping("/admin/do/login.html")
    public String doLogin(
            @RequestParam("loginAcct" )String loginAcct ,
            @RequestParam("userPswd") String userPswd,
            HttpSession session){

        // 1.判断密码是否非法
        if(userPswd.length() ==0 || userPswd == null){
            throw new LoginFailedException(CrowdConstant.MESSAGE_LOGIN_FAILED);
        }
        // 2.查询用户
        // 如果在数据库中查询到了用户，说明登录成功
        Admin admin  = adminService.getAdminByLoginAcct(loginAcct,userPswd);

        // 3.将查询的admin保存到作用域
        session.setAttribute(CrowdConstant.ATTR_NAME_LOGIN_ADMIN,admin);

        // 4.重定向到 去查询用户组所有的数据
        return "redirect:/admin/to/main/page.html";

    }




}
