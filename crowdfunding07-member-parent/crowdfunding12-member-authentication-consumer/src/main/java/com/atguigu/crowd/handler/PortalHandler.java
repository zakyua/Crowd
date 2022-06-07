package com.atguigu.crowd.handler;

import com.atguigu.crowd.api.MySQLRemoteService;
import com.atguigu.crowd.constant.CrowdConstant;
import com.atguigu.crowd.entity.vo.PortalTypeVO;
import com.atguigu.crowd.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @author ChenCheng
 * @create 2022-05-31 21:55
 */
@Controller
public class PortalHandler {

    @Autowired
    private MySQLRemoteService mySQLRemoteService;


    /**
     *   跳转首页的方法
     * @return
     */
    @RequestMapping("/")
    public String  showPortalPage(ModelMap modelMap){

        // 1.去数据库中查出将来要在页面上显示的数据
        ResultEntity<List<PortalTypeVO>> resultEntity = mySQLRemoteService.getPortalTypeProjectDataRemote();
        // 如果操作成功，将得到的list加入请求域
        if (ResultEntity.SUCCESS.equals(resultEntity.getResult())){
            List<PortalTypeVO> portalTypeVOList = resultEntity.getData();
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_PORTAL_TYPE_LIST,portalTypeVOList);
        }
        // 2.返回到首页
        return "portal";
    }



}
