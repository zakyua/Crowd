package com.atguigu.crowd.Test;


import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;


import com.atguigu.crowd.entity.Admin;
import com.atguigu.crowd.util.CrowdUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


import com.atguigu.crowd.mapper.AdminMapper;
import com.atguigu.crowd.service.api.AdminService;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-persist-mybatis.xml","classpath:spring-persist-tx.xml"})
public class CrowdTest {

	
	@Autowired
	private DataSource dataSource;
	
	@Autowired
	private AdminMapper adminMapper;
	
	
	@Autowired
	private AdminService adminService;
	
	
	
	//测试连接
	@Test
	public void textList() {
		List<Admin> list = adminService.getAll();
		list.forEach(System.out::println);

	} 
		


	// 插入假数据
	@Test
	public void testInsert(){

		for (int i = 0; i < 238; i++) {
			adminMapper.insert(new Admin(null,"loginAcct"+i,"userPswd"+i,"userName"+i,"email"+i,null));
		}

	}
	
	
	@Test
	public void testTX() {

		String md5 = "123123";
		String passWord = CrowdUtil.md5(md5);

		adminService.saveAdmin(new Admin(null,"tom",passWord,"杰克","tom@qq.com","20220508"));
		
		
	}	
	
	
	
	
	
	@Test
	public void testInsertAdmin() {

		adminMapper.insert(new Admin(null,"tom","123123","汤姆","tom@qq.com","20220508")


		);
	}
	
	@Test
	public void testConnection() throws SQLException {
		Connection connection = dataSource.getConnection();
		System.out.println(connection);
		
	}
	
	
	
	
}
