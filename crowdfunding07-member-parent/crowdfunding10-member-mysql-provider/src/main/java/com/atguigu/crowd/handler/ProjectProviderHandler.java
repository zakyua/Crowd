package com.atguigu.crowd.handler;

import com.atguigu.crowd.Service.api.ProjectService;
import com.atguigu.crowd.entity.vo.DetailProjectVO;
import com.atguigu.crowd.entity.vo.PortalTypeVO;
import com.atguigu.crowd.entity.vo.ProjectVO;
import com.atguigu.crowd.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author ChenCheng
 * @create 2022-06-03 17:53
 */
@RestController
public class ProjectProviderHandler {


    @Autowired
    private ProjectService projectService;

    /**
     *   将ProjectVO中的数据存入各个数据库
     * @param projectVO
     * @param memberId
     * @return
     */
    @RequestMapping("/save/project/remote")
    public ResultEntity<String> saveProjectRemote(@RequestBody ProjectVO projectVO, @RequestParam("memberId") Integer memberId){
        // 调用本地service进行保存
        try {
            projectService.saveProject(projectVO, memberId);
            return ResultEntity.successWithOutData();
        } catch (Exception e){
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }

    }



    /**
     *   查询网站主页面需要的项目信息
     *      需要将项目信息查询出来，分类信息查询出来
     * @return
     */
    @RequestMapping("/get/portal/type/project/data/remote")
    public ResultEntity<List<PortalTypeVO>> getPortalTypeProjectDataRemote(){

        try {
            List<PortalTypeVO> portalTypeVOList = projectService.getPortalTypeVOList();
            return ResultEntity.successWithData(portalTypeVOList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }


    }


    /**
     * 查询出项目的详情信息
     * @param projectId
     * @return
     */
    @RequestMapping("/get/detail/project/remote/{projectId}")
    public ResultEntity<DetailProjectVO> getDetailProjectVORemote(@PathVariable("projectId") Integer projectId){
        try {
            DetailProjectVO detailProjectVO = projectService.getDetailProjectVO(projectId);
            return ResultEntity.successWithData(detailProjectVO);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }





}
