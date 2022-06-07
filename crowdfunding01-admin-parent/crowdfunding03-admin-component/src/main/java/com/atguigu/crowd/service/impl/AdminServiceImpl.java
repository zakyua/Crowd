package com.atguigu.crowd.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;


import com.atguigu.crowd.constant.CrowdConstant;
import com.atguigu.crowd.entity.Role;
import com.atguigu.crowd.exception.LoginAcctAlreadyInUseException;
import com.atguigu.crowd.exception.LoginAcctAlreadyInUseForUpdateException;
import com.atguigu.crowd.exception.LoginFailedException;
import com.atguigu.crowd.mapper.AdminMapper;
import com.atguigu.crowd.service.api.AdminService;
import com.atguigu.crowd.entity.Admin;
import com.atguigu.crowd.entity.AdminExample;
import com.atguigu.crowd.service.api.AuthService;
import com.atguigu.crowd.service.api.RoleService;
import com.atguigu.crowd.util.CrowdUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl implements AdminService {

	@Autowired
	private AdminMapper adminMapper;

	@Autowired
	private RoleService roleService;

	@Autowired
	private AuthService authService;


	@Autowired
	private BCryptPasswordEncoder passwordEncoder;




	private Logger logger = LoggerFactory.getLogger(AdminServiceImpl.class);


	//这个业务类中的方法都是家里事务的
	
	/**
	 * 插入一条数据
	 */
	@Override
	public void saveAdmin(Admin admin) {

		// 对密码进行加密
		String ordUserPswd = admin.getUserPswd();
		// String newUserPswd = CrowdUtil.md5(ordUserPswd);
		// 使用springSecurity提供的加密方式来加密
		String newUserPswd = passwordEncoder.encode(ordUserPswd);

		admin.setUserPswd(newUserPswd);

		// 创建生成时间
		Date date = new Date();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String format = simpleDateFormat.format(date);
		admin.setCreateTime(format);

		try {
			adminMapper.insert(admin);
		}catch (Exception e){
			e.printStackTrace();
			logger.info("异常全类名"+e.getClass().getName());
			if(e instanceof DuplicateKeyException){
				throw new LoginAcctAlreadyInUseException(CrowdConstant.MESSAGE_LOGIN_ACCT_ALREADY_IN_USE);
			}
		}

	}


	/**
	 *  查询所有的数据
	 */
	@Override
	public List<Admin> getAll() {
		List<Admin> adminList = adminMapper.selectByExample(new AdminExample());
		return adminList;
	}

	// 登录功能
	@Override
	public Admin getAdminByLoginAcct(String loginAcct, String userPassword) {

		// 1.根据登录账号查询Admin对象
		// ①创建AdminExample对象
		AdminExample adminExample = new AdminExample();
		// ②创建Criteria对象
		AdminExample.Criteria criteria = adminExample.createCriteria();
		// ③在Criteria对象中封装查询条件
		criteria.andLoginAcctEqualTo(loginAcct);
        // ④调用AdminMapper的方法执行查询
		List<Admin> adminList = adminMapper.selectByExample(adminExample);

		// 2.判断Admin对象
		if(adminList.size() == 0 || adminList == null){
			throw new LoginFailedException(CrowdConstant.MESSAGE_LOGIN_FAILED);
		}
		// ①当前用户不止一个
		if(adminList.size() > 1){
			throw new LoginFailedException(CrowdConstant.MESSAGE_SYSTEM_ERROR_LOGIN_NOT_UNIQUE);
		}

		Admin admin = adminList.get(0);

		// 3.如果Admin对象为null则抛出异常
		if(admin == null){
			throw new LoginFailedException(CrowdConstant.MESSAGE_LOGIN_FAILED);
		}

		// 4.比较前端的密码和数据查询出的密码是否一致
		if(!(Objects.equals(CrowdUtil.md5(userPassword), admin.getUserPswd()))){
			throw new LoginFailedException(CrowdConstant.MESSAGE_LOGIN_FAILED);
		}

		// 5.返回admin对象
		return admin;
	}


	/**
	 *  根据关键字和当前的页码和每页的数量 得到数据
	 * @param keyword
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	@Override
	public PageInfo<Admin> getPageInfo(String keyword, Integer pageNum, Integer pageSize) {

		// 1.调用PageHelper的静态方法，开启分页功能
		PageHelper.startPage(pageNum,pageSize);
		// 2.执行查询
		List<Admin> list = adminMapper.selectAdminListByKeyword(keyword);

		// 3.封装到PageInfo中
		return new PageInfo<>(list);
	}

	/**
	 * 删除用户管理员
	 * @param adminId
	 */
	@Override
	public void remove(Integer adminId) {
		adminMapper.deleteByPrimaryKey(adminId);
	}


	/**
	 *  查询用户
	 * @param adminId
	 * @return
	 */
	@Override
	public Admin getAdminById(Integer adminId) {
		return adminMapper.selectByPrimaryKey(adminId);
	}

	/**
	 *  修改用户
	 * @param admin
	 */
	@Override
	public void updateAdmin(Admin admin) {

		// "Selective"表示有选择的插入
		try {
			adminMapper.updateByPrimaryKeySelective(admin);
		} catch (Exception e) {
			e.printStackTrace();
			if(e instanceof DuplicateKeyException){
				throw new LoginAcctAlreadyInUseForUpdateException(CrowdConstant.MESSAGE_LOGIN_ACCT_ALREADY_IN_USE);
			}
			// 为了不掩盖异常，出现的异常不是DuplicateKeyException类型的，将捕捉到的异常向上抛
			throw e;
		}


	}

	/**
	 *  修改用户的角色信息
	 * @param adminId
	 * @param roleIdList
	 */
	@Override
	public void saveAdminRoleRelationship(Integer adminId, List<Integer> roleIdList) {


		// 1.删除当前用户的角色信息
           adminMapper.clearOldRelationship(adminId);

		// 2.添加当前用户的角色信息
		if(roleIdList != null && roleIdList.size() > 0){
			adminMapper.insertNewRelationship(adminId,roleIdList);
		}

	}

	/**
	 *  根据用户名查询出用户
	 * @param username
	 * @return
	 */
	@Override
	public Admin getAdminByLoginAcct(String username) {

		AdminExample adminExample = new AdminExample();
		AdminExample.Criteria criteria = adminExample.createCriteria();
		criteria.andLoginAcctEqualTo(username);

		List<Admin> adminList = adminMapper.selectByExample(adminExample);
		Admin admin = adminList.get(0);

		return admin;
	}


	/**
	 *  根据当前用户的id去把这个用户拥有的权限查询出来
	 * @param adminId
	 * @return
	 */
	@Override
	public List<String> getAssignedAuthNameByAdminId(Integer adminId) {
		//
		ArrayList<Integer> roleIdList = new ArrayList<>();


		// 1.先查询角色
		List<Role> roleList = roleService.getAssignedRole(adminId);
		for (Role role :
				roleList
				) {
			roleIdList.add(role.getId());
		}

		if(roleIdList != null && roleIdList.size() >0){

			// 2.查询角色的权限(在中间表去查)
			List<String> authNameList = authService.getAuthByRoleIdList(roleIdList);

		}

		return null;
	}


}
