package com.atguigu.crowd.Service.impl;

import com.atguigu.crowd.Service.api.ProjectService;
import com.atguigu.crowd.entity.po.MemberConfirmInfoPO;
import com.atguigu.crowd.entity.po.MemberLaunchInfoPO;
import com.atguigu.crowd.entity.po.ProjectPO;
import com.atguigu.crowd.entity.po.ReturnPO;
import com.atguigu.crowd.entity.vo.*;
import com.atguigu.crowd.mapper.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author ChenCheng
 * @create 2022-06-03 17:55
 */
@Transactional(readOnly = true)
@Service
public class projectServiceImpl implements ProjectService {


    @Autowired(required = false)
    private ProjectPOMapper projectPOMapper;
    @Autowired(required = false)
    private ProjectItemPicPOMapper projectItemPicPOMapper;
    @Autowired(required = false)
    private MemberLaunchInfoPOMapper memberLaunchInfoPOMapper;
    @Autowired(required = false)
    private MemberConfirmInfoPOMapper memberConfirmInfoPOMapper;
    @Autowired(required = false)
    private ReturnPOMapper returnPOMapper;



    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    @Override
    public void saveProject(ProjectVO projectVO, Integer memberId) {
        // 一、保存projectPo
          // 1.准备一个ProjectPo
        ProjectPO projectPO = new ProjectPO();

          // 2.利用工具方法，给ProjectPO赋值
        BeanUtils.copyProperties(projectVO,projectPO);

           // 3.给projectPO的其余属性赋值
             // 3.1设置项目发起人的id
        projectPO.setMemberid(memberId);
              // 3.2设置项目发起时间
        String createDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        projectPO.setDeploydate(createDate);
              // 3.3设置项目当前的状态
        projectPO.setStatus(0);

         // 4.像数据中保存projectPO
            /*
            (有一些属性还没有值，所以选择性插入。我们后期还需要将ProjectVO中的其他值保存到别的表中，所以我们需要数据库中这个项目表信息的id)
            (在xml文件中配置insert标签增加useGeneratedKeys="true" keyProperty="id")
             */
        projectPOMapper.insertSelective(projectPO);

        // 5.获取projectPO的id
        Integer projectPOId = projectPO.getId();

        // 二、保存分类信息到数据库表中
            // 1.获取分类信息的id
        List<Integer> typeIdList = projectVO.getTypeIdList();
             // 2.将项目的id和分类信息的id保存到项目和分类信息的中间表中
        projectPOMapper.saveTypeRelationship(projectPOId,typeIdList);

        // 三、保存标签信息到数据库表中
             // 1.获取标签的id集合
        List<Integer> tagIdList = projectVO.getTagIdList();
             // 2.将项目的id和标签信息保存到项目和标签信息的中间表中
        projectPOMapper.saveTagRelationship(projectPOId,tagIdList);

        // 四、保存详情图片的路径到数据库
           // 1.获取图片详情的地址集合
        List<String> detailPicturePathList = projectVO.getDetailPicturePathList();
           // 2.保存图片详情地址
        projectItemPicPOMapper.insertPathList(projectPOId,detailPicturePathList);

        // 五、保存项目发起人信息
           // 1.取出项目发起人信息
        MemberLauchInfoVO memberLauchInfoVO = projectVO.getMemberLauchInfoVO();
           // 2.准备MemberLaunchInfoPO
        MemberLaunchInfoPO memberLaunchInfoPO = new MemberLaunchInfoPO();
           // 3.给MemberLaunchInfoPO设置属性
        BeanUtils.copyProperties(memberLauchInfoVO,memberLaunchInfoPO);
           // 4.设置会员id
        memberLaunchInfoPO.setMemberid(memberId);
           // 5.保存项目发起人的信息
        memberLaunchInfoPOMapper.insertSelective(memberLaunchInfoPO);

        // 六、保存发起人确认信息
           // 1.获取项目发起人信息
        MemberConfirmInfoVO memberConfirmInfoVO = projectVO.getMemberConfirmInfoVO();
           // 2.准备MemberConfirmInfoPO
        MemberConfirmInfoPO memberConfirmInfoPO = new MemberConfirmInfoPO();
           // 3.给MemberConfirmInfoPO设置属性
        BeanUtils.copyProperties(memberConfirmInfoVO,memberConfirmInfoPO);
           // 4.设置会员id
        memberConfirmInfoPO.setMemberid(memberId);
           // 5.保存项目发起人信息
        memberConfirmInfoPOMapper.insertSelective(memberConfirmInfoPO);

        // 七、保存汇报信息
           // 1.获取回报信息的集合
        List<ReturnVO> returnVOList = projectVO.getReturnVOList();
           // 2.准备一个存放数据库的回报信息集合
        List<ReturnPO> returnPOList = new ArrayList<>();
           // 3.遍历returnVOList
        for (ReturnVO returnVO :
                returnVOList
        ) {
            // 4.准备回报信息的Po
            ReturnPO returnPO = new ReturnPO();
            BeanUtils.copyProperties(returnVO,returnPO);
            // 5.将回报信息的Po放入集合中
            returnPOList.add(returnPO);
        }
           // 6.保存回报信息的集合
        returnPOMapper.insertReturnPOList(projectPOId,returnPOList);
    }




    @Override
    public List<PortalTypeVO> getPortalTypeVOList() {
    return  projectPOMapper.selectPortalTypeVOList();
    }



    @Override
    public DetailProjectVO getDetailProjectVO(Integer projectId) {
        // 1.查询出项目的详情信息
        DetailProjectVO detailProjectVO = projectPOMapper.selectDetailProjectVO(projectId);

        // 2.根据项目的状态设置状态信息
        Integer status = detailProjectVO.getStatus();
        switch (status){
            case 0:
                detailProjectVO.setStatusText("即将开始");
                break;
            case 1:
                detailProjectVO.setStatusText("众筹中");
                break;
            case 2:
                detailProjectVO.setStatusText("众筹成功");
                break;
            case 3:
                detailProjectVO.setStatusText("众筹失败");
                break;
            default:
                break;
        }

        // 3.计算出项目的剩余时间
        String deployDate = detailProjectVO.getDeployDate();
        // 3.1获取当前日期
        Date currentDay = new Date();
        // 3.2把众筹的日期转换为Date类型
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date deployDay = format.parse(deployDate);

            // 4.获取当前日期的时间戳
            long currentDayTime = currentDay.getTime();

            // 5.获取众筹日期的时间戳
            long deployDayTime = deployDay.getTime();

            // 6.通过当前日期时间戳-众筹日期时间戳，得到已经去过的时间
            Integer passDay = (int)(currentDayTime - deployDayTime) / 1000 / 60 / 60 / 24;

            // 7.用设置的众筹天数-过去的时间 得到剩余时间
            int lastDay = detailProjectVO.getDay() - passDay;

            // 8.给当前的DetailProjectVO对象设置剩余时间
            detailProjectVO.setLastDay(lastDay);

        } catch (ParseException e) {
            e.printStackTrace();
        }


        return detailProjectVO;
    }


}
