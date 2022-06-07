package com.atguigu.crowd.service.api;



import com.atguigu.crowd.entity.Admin;
import com.github.pagehelper.PageInfo;

import java.util.List;



public interface AdminService {

	/**
	 * 插入一条数据
	 * @param admin
	 */
	void saveAdmin(Admin admin);
	
	/**
	 * 插入一条数据
	 */
	
	List<Admin> getAll();

	/**
	 *  根据用户名和密码查询用户
	 * @param loginAcct
	 * @param userPassword
	 * @return
	 */
    Admin getAdminByLoginAcct(String loginAcct, String userPassword);


    PageInfo<Admin> getPageInfo(String keyword,Integer pageNum,Integer pageSize);


	void remove(Integer adminId);

	Admin getAdminById(Integer adminId);

	void updateAdmin(Admin admin);

    void saveAdminRoleRelationship(Integer adminId, List<Integer> roleIdList);

	Admin getAdminByLoginAcct(String username);

	List<String> getAssignedAuthNameByAdminId(Integer adminId);
}
