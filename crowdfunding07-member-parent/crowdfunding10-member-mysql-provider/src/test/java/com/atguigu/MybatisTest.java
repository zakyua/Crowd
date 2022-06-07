package com.atguigu;

import com.atguigu.crowd.MysqlMainClass;
import com.atguigu.crowd.entity.po.MemberPO;
import com.atguigu.crowd.entity.po.MemberPOExample;
import com.atguigu.crowd.mapper.MemberPOMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * @author ChenCheng
 * @create 2022-05-31 18:35
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MysqlMainClass.class)
public class MybatisTest {


    @Autowired(required = false)
    private MemberPOMapper memberPOMapper;


    @Autowired
    private DataSource dataSource;

    @Test
    public void getMember() throws SQLException {
        Connection connection = dataSource.getConnection();
        System.out.println(connection);

    }



    @Test
    public void getAll(){

        List<MemberPO> memberPOS = memberPOMapper.selectByExample(new MemberPOExample());

        memberPOS.forEach(System.out::println);
    }



}
