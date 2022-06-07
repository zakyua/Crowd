package com.atguigu.crowd.Handler;

import com.atguigu.crowd.api.MySQLRemoteService;
import com.atguigu.crowd.config.OSSProperties;
import com.atguigu.crowd.constant.CrowdConstant;
import com.atguigu.crowd.entity.vo.*;
import com.atguigu.crowd.util.CrowdUtil;
import com.atguigu.crowd.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ChenCheng
 * @create 2022-06-02 18:26
 */
@Controller
public class ProjectConsumerHandler {


    @Autowired
    private OSSProperties ossProperties;

    @Autowired
    private MySQLRemoteService mySQLRemoteService;




    /**
     *      处理项目及发起人信息的表单提交
     * @param projectVO                 接收表单发来数据
     * @param headerPicture             接收头图
     * @param detailPictureList         接收项目详情图片
     * @param session
     * @param modelMap                  向与对象存入数据
     * @return
     */
    @RequestMapping("/create/project/information")
    public String  createProject(
            ProjectVO projectVO,
            MultipartFile headerPicture,
            List<MultipartFile> detailPictureList,
            HttpSession session,
            ModelMap modelMap
    ) throws IOException {

        // 1.完成头图的上传
           // 1.1 检查头图是否正常
        if(headerPicture.isEmpty()){
            // 头图为空，返回当前页面，显示错误信息
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE,CrowdConstant.MESSAGE_HEADER_PIC_EMPTY);
            return "project-launch";
        }
            // 1.2 上传头图到oos
        ResultEntity<String> uploadResultEntity = CrowdUtil.uploadFileToOSS(
                ossProperties.getEndPoint(),
                ossProperties.getAccessKeyId(),
                ossProperties.getAccessKeySecret(),
                headerPicture.getInputStream(),
                ossProperties.getBucketName(),
                ossProperties.getBucketDomain(),
                headerPicture.getOriginalFilename()
        );

        String result = uploadResultEntity.getResult();
            // 1.3 判断删除图片是否成功
        if(ResultEntity.FAILED.equals(result)){
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE,CrowdConstant.MESSAGE_HEADER_PIC_UPLOAD_FAILED);
            return "project-launch";
        }
             // 1.4 上传成功，获取图片在oos上的位置
        String headerPicturePath = uploadResultEntity.getData();
             // 1.5 存入ProjectVO对象
        projectVO.setHeaderPicturePath(headerPicturePath);

        // 2.上传详情图片
           // 2.1 准备一个detailPicturePathList来存放详情图片的位置
        List<String> detailPicturePathList = new ArrayList<>();
           // 2.2检查详情图片是否正常
        if(detailPictureList == null || detailPictureList.size() ==0 ){
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE,CrowdConstant.MESSAGE_DETAIL_PIC_EMPTY);
            return "project-launch";
        }
           // 2.3取出项目图片
        for (MultipartFile detailPicture:
                detailPictureList
             ) {
            // 2.4判断详情图片是否为空
            if(detailPicture.isEmpty()){
                modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE,CrowdConstant.MESSAGE_DETAIL_PIC_EMPTY);
                return "project-launch";
            }
            // 2.5上传项目详情图片
            ResultEntity<String> uploadDetailPicResultEntity = CrowdUtil.uploadFileToOSS(
                    ossProperties.getEndPoint(),
                    ossProperties.getAccessKeyId(),
                    ossProperties.getAccessKeySecret(),
                    detailPicture.getInputStream(),
                    ossProperties.getBucketName(),
                    ossProperties.getBucketDomain(),
                    detailPicture.getOriginalFilename()
            );
            // 2.6判断上传是否成功
            String detailPicResult = uploadDetailPicResultEntity.getResult();
            if(ResultEntity.FAILED.equals(detailPicResult)){
                modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE,CrowdConstant.MESSAGE_DETAIL_PIC_UPLOAD_FAILED);
                return "project-launch";
            }
            // 2.7 获取项目图片的地址
            String uploadDetailPicData = uploadDetailPicResultEntity.getData();
            detailPicturePathList.add(uploadDetailPicData);
        }


        // 3.将detailPicturePathList设置给projectVO
        projectVO.setDetailPicturePathList(detailPicturePathList);

        // 4.将projectVO保存到session中(收集信息的过程还没有结束，先将projectVO保存在session中，在下一个汇报页面继续收集)

        session.setAttribute(CrowdConstant.ATTR_NAME_TEMPLE_PROJECT, projectVO);

        // 5.进入下一个收集回报信息的页面(进入下一个汇报的页面继续收集returnVo和xxxVO,最后在汇总到一个PO中保存在数据库中)
        return "redirect:http://localhost/project/return/info/page.html";
    }


    /**
     *   回报页面上传汇报的图片
     * @param returnPicture
     * @return
     */
    @RequestMapping("/create/upload/return/picture.json}")
    public ResultEntity<String> uploadReturnPicture(@RequestParam("returnPicture") MultipartFile returnPicture) throws IOException {


        // 1.判断上传的图片是否为空
        if(returnPicture.isEmpty()){
            // 上传的图片为空
            return ResultEntity.failed(CrowdConstant.MESSAGE_RETURN_PIC_EMPTY);
        }

        // 2.上传图片
        ResultEntity<String> uploadReturnPictureEntity = CrowdUtil.uploadFileToOSS(
                ossProperties.getEndPoint(),
                ossProperties.getAccessKeyId(),
                ossProperties.getAccessKeySecret(),
                returnPicture.getInputStream(),
                ossProperties.getBucketName(),
                ossProperties.getBucketDomain(),
                returnPicture.getOriginalFilename()
        );
        // 3.判断上传图片是否成功
        return uploadReturnPictureEntity;
    }


    /**
     *        收集项目汇报的信息
     * @param returnVO
     * @param session
     * @return
     */
    @RequestMapping("/create/save/return.json")
    public ResultEntity<String> saveReturn(ReturnVO returnVO ,HttpSession session){
        try {
            // 1.先从session中取出projectVO
            ProjectVO projectVO = (ProjectVO) session.getAttribute(CrowdConstant.ATTR_NAME_TEMPLE_PROJECT);

            // 2.判断ProjectVO是否回null
            if (projectVO == null){
                return ResultEntity.failed(CrowdConstant.MESSAGE_TEMPLE_PROJECT_MISSING);
            }

            // 3.从projectVO取出returnVOList
            List<ReturnVO> returnVOList = projectVO.getReturnVOList();
            if(returnVOList == null){
                // 3.1 如果projectVO中的returnVOList为空创建一个集合在赋给projectVO，避免空指针
                returnVOList =  new ArrayList<>();
                projectVO.setReturnVOList(returnVOList);
            }
            // 4.将前端发来的returnVO保存到returnVOList中
            returnVOList.add(returnVO);

            // 5.将projectVO重新保存到session中
            session.setAttribute(CrowdConstant.ATTR_NAME_TEMPLE_PROJECT, projectVO);

            // 6.成功返回成功信息
            return ResultEntity.successWithOutData();
        } catch (Exception e) {
            e.printStackTrace();
             // 7.失败返回失败的信息
            return  ResultEntity.failed(e.getMessage());
        }

    }


    /**
     *        提交确认信息，将所有的数据保存在数据库中
     * @param memberConfirmInfoVO   项目的确认信息
     * @param session
     * @param modelMap
     * @return
     */
    @RequestMapping("/create/confirm.html")
    public  String saveConfirm(MemberConfirmInfoVO memberConfirmInfoVO,HttpSession session,ModelMap modelMap){

        // 1.先从session取出projectVO
        ProjectVO projectVO = (ProjectVO)session.getAttribute(CrowdConstant.ATTR_NAME_TEMPLE_PROJECT);
        // 2.判断这个projectVO是否为空
        if(projectVO == null){
            // 如果每个获取在前期没有收集到这个projectVO里的信息，直接报异常
            throw new RuntimeException(CrowdConstant.MESSAGE_TEMPLE_PROJECT_MISSING);
        }
        // 3.projectVO正常，将memberConfirmInfoVO放入projectVO
        projectVO.setMemberConfirmInfoVO(memberConfirmInfoVO);
        // 4.从session域中取出当前登录的用户
        LoginMemberVO loginMemberVO = (LoginMemberVO)session.getAttribute(CrowdConstant.ATTR_NAME_LOGIN_MEMBER);
        // 5.获取当前用户的id
        Integer memberId = loginMemberVO.getId();
        // 6.调用远程的方法保存projectVO到数据库中
        ResultEntity<String> saveResultEntity = mySQLRemoteService.saveProjectRemote(projectVO, memberId);
        // 7.判断保存数据是否出错
        String result = saveResultEntity.getResult();
        if(ResultEntity.FAILED.equals(result)){
            // 保存出错，返回确认的界面，并且携带错误的消息
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, saveResultEntity.getMessage());
            return "project-confirm";
        }
        // 保存正常完成，删除session中临时存放的ProjectVO
        session.removeAttribute(CrowdConstant.ATTR_NAME_TEMPLE_PROJECT);
        // 8.重定向到成功的页面，避免重复提交表单
        return "redirect:http://localhost/project/create/success.html";
    }


    /**
     *  查看项目的细节详情
     * @param projectId
     * @param modelMap
     * @return
     */
    @RequestMapping("/show/project/detail/{projectId}")
    public String getDetailProject(@PathVariable("projectId") Integer projectId, ModelMap modelMap){


        // 1.根据id去数据库查询出来这个项目的详情
        ResultEntity<DetailProjectVO> resultEntity = mySQLRemoteService.getDetailProjectVORemote(projectId);
        if (ResultEntity.SUCCESS.equals(resultEntity.getResult())){
            DetailProjectVO detailProjectVO = resultEntity.getData();
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_DETAIL_PROJECT,detailProjectVO);
        }
        return "project-show-detail";
    }


}
