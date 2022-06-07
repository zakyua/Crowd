package com.atguigu.crowd.service.impl;

import com.atguigu.crowd.entity.Menu;
import com.atguigu.crowd.entity.MenuExample;
import com.atguigu.crowd.mapper.MenuMapper;
import com.atguigu.crowd.service.api.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ChenCheng
 * @create 2022-05-20 12:59
 */
@Service
public class MenuServiceImpl implements MenuService {

    @Autowired
    private MenuMapper menuMapper;



    // 获取所有的数据
    @Override
    public List<Menu> getAll() {
        return menuMapper.selectByExample(new MenuExample());
    }

    // 添加数据
    @Override
    public void saveMenu(Menu menu) {
        menuMapper.insert(menu);
    }

    // 更新数据
    @Override
    public void updateMenu(Menu menu) {
        // 选择有选择的更新，因为父节点是不可以更新的
        menuMapper.updateByPrimaryKeySelective(menu);
    }

    /**
     * 删除一个节点
     * @param id
     */
    @Override
    public void removeMenu(Integer id) {
        menuMapper.deleteByPrimaryKey(id);
    }


}
