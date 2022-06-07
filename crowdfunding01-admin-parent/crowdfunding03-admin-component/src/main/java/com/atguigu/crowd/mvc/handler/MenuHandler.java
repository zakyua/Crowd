package com.atguigu.crowd.mvc.handler;

import com.atguigu.crowd.entity.Menu;
import com.atguigu.crowd.service.api.MenuService;
import com.atguigu.crowd.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author ChenCheng
 * @create 2022-05-20 13:00
 */
@Controller
public class MenuHandler {
    @Autowired
    private MenuService menuService;


    /**
     * 添加信息
     * @return
     */
    @ResponseBody
    @RequestMapping("/menu/save.json")
    public ResultEntity<String> saveMenu(Menu menu){
        menuService.saveMenu(menu);
        return ResultEntity.successWithOutData();
    }

    /**
     * 更新数据
     * @param menu
     */
    @ResponseBody
    @RequestMapping("/menu/update.json")
    public ResultEntity<String> updateMenu(Menu menu){

        menuService.updateMenu(menu);
        return ResultEntity.successWithOutData();
    }


    @ResponseBody
    @RequestMapping("/menu/remove.json")
    public ResultEntity<String> removeMenu(@RequestParam("id") Integer id){

        menuService.removeMenu(id);
        return  ResultEntity.successWithOutData();

    }

    /**
     * 查询所有数据
     * @return
     */
    @ResponseBody
    @RequestMapping("/menu/get/whole/tree.json")
    public ResultEntity<Menu> getWholeNew(){
        // 返回一个树形结构的结构，将来在页面进行展示
        // 对Old的判断进行优化
        List<Menu> menuList = menuService.getAll();
        // 定义一个根节点
        Menu root = null;
        // 准备一个map来存放menu的id和menu，建立对应关系
        Map<Integer,Menu> menuMap = new HashMap<>();

        // 遍历所有的menu
        for (Menu menu:
             menuList) {
            // 将所有的menu保存到一个map中去
           menuMap.put(menu.getId(),menu);
        }

        // 给所有的节点设置父节点
        for (Menu menu :
                menuList) {
            // 这是一个根节点不需要为其设置父节点
            if(menu.getPid() == null){
                root = menu;
                continue;
            }

            // 为当前的menu设置父节点
            Menu fatherMenu = menuMap.get(menu.getPid());
            fatherMenu.getChildren().add(menu);
        }
        return ResultEntity.successWithData(root);
    }




    public ResultEntity<Menu> getWholeOld(){
        // 获取到所有的菜单项
        List<Menu> menuList = menuService.getAll();

        Menu root = null;

        // 给有子节点的父亲菜单项赋值(这是在一个找爸爸的过程)
        for (Menu menu :
               menuList ) {
            Integer pid = menu.getPid();
            // 这个是根节点
            if(pid == null){
                root = menu;
                // 跳出本次循环,判断下一个menu
                continue;
            }
            // 去过pip部位null就要给这些菜单项赋值
            // 这是当前这个menu的父节点
            for (Menu maybeFather:
                    menuList) {
                // 这个当前menu的id
                Integer id = maybeFather.getId();
                // 如果当前menu的id和某一个元素的pid相等，则这个maybeFather就是这个menu的父节点
                if(Objects.equals(pid,id)){
                    maybeFather.getChildren().add(menu);
                }

            }

        }

        // 将root返回
        return ResultEntity.successWithData(root);





    }



}
