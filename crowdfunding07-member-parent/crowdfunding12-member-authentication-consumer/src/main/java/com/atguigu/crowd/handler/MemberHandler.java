package com.atguigu.crowd.handler;

import com.atguigu.crowd.api.MySQLRemoteService;
import com.atguigu.crowd.api.RedisRemoteService;
import com.atguigu.crowd.config.ShortMessageProperties;
import com.atguigu.crowd.constant.CrowdConstant;
import com.atguigu.crowd.entity.po.MemberPO;
import com.atguigu.crowd.entity.vo.LoginMemberVO;
import com.atguigu.crowd.entity.vo.MemberVO;
import com.atguigu.crowd.util.CrowdUtil;
import com.atguigu.crowd.util.ResultEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author ChenCheng
 * @create 2022-06-01 9:11
 */

@Controller
public class MemberHandler {


    @Autowired
    private MySQLRemoteService mySQLRemoteService;
    @Autowired
    private RedisRemoteService redisRemoteService;
    @Autowired
    private ShortMessageProperties shortMessageProperties;




// 处理注册的请求
    @RequestMapping("/auth/member/do/register.html")
    public String doMemberRegister( MemberVO memberVO ,ModelMap modelMap){
        // 1.获取手机号
        String phoneNum = memberVO.getPhoneNum();
        // 2.进行验证码检验
           // 2.1拼接key值
        String key = CrowdConstant.REDIS_CODE_PREFIX + phoneNum;
           // 2.2根据kei在redis中查询出验证码
        ResultEntity<String> redisValueByKeyRemote = redisRemoteService.getRedisValueByKeyRemote(key);
        // 3.判断查询的结果
        String redisSelectResult = redisValueByKeyRemote.getResult();
        if(ResultEntity.FAILED.equals(redisSelectResult)){
            // 失败，需要返回到注册的页面，并且打印信息
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE,redisValueByKeyRemote.getMessage());
            return "member-reg";
        }
            // 成功，获取redis中的验证码  code:1145
        String redisCode = redisValueByKeyRemote.getData();

        // 4.判断验证码是否为空
        if(redisCode == null){
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE,CrowdConstant.MESSAGE_CODE_NOT_EXIST);
            return "member-reg";
        }
        // 5.将表单上的验证码和redis中保存的验证进行比较
        String formCode = memberVO.getCode();
        if(!Objects.equals(formCode,redisCode)){
            // 不相等，返回主页，打印信息
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE,CrowdConstant.MESSAGE_CODE_INVALID);
            return "member-reg";
        }
        // 6.验证码相同,删除redis中验证码数据
        ResultEntity<String> stringResultEntity = redisRemoteService.removeRedisKeyByKeyRemote(key);
        // 7.判断删除返回的结果
        String redisDeleteResult = stringResultEntity.getResult();
        if(ResultEntity.FAILED.equals(redisDeleteResult)){
            // 删除的结果不需要在页面显示，还是使用日志记录一下，避免redis命中率降低
           // log.info("redis数据库验证码删除失败");
        }
        // 8.将注册的用户信息保存到mySQL数据库中
           // 8.1获取用户的密码
        String userBeforeswd = memberVO.getUserPswd();
           // 8.2判断密码是否有效
        if(userBeforeswd == null || userBeforeswd.length() == 0){
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE,CrowdConstant.MESSAGE_STRING_INVALIDATE);
            return "member-reg";
        }
          // 8.3对密码进行加密
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String userAfterPswd = passwordEncoder.encode(userBeforeswd);
          // 8.4将加密后的密码重新设置给memberVO
        memberVO.setUserPswd(userAfterPswd);
          // 8.5将memberVO赋值给memberPo保存到mysql数据库
        MemberPO memberPO = new MemberPO();
        BeanUtils.copyProperties(memberVO,memberPO);
          // 8.6保存memberPO到mysql数据库
        ResultEntity<String> saveMemberRemote = mySQLRemoteService.saveMemberRemote(memberPO);
        // 9.判断添加用户是否成功
        String saveMemberRemoteResult = saveMemberRemote.getResult();
        if(ResultEntity.FAILED.equals(saveMemberRemoteResult)){
            // 添加失败，返回添加家失败的信息，返回到注册的页面
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE,saveMemberRemote.getMessage());
            return "member-reg";
        }

        // 10.添加成功，跳转到登录的主页面(因为网关是挡在最前面的，所以需要加上域名)
        return "redirect:http://localhost/auth/to/member/login/page.html";
    }


    /**
     *   注册用户时，发送验证码
     * @param phoneNum  需要发送验证码的手机
     * @return
     */
    @ResponseBody
    @RequestMapping("/auth/member/send/short/message.json")
    public ResultEntity<String> shortMessage(@RequestParam("phoneNum") String phoneNum){

        String host = shortMessageProperties.getHost();
        String path = shortMessageProperties.getPath();
        String method = shortMessageProperties.getMethod();
        String appcode = shortMessageProperties.getAppCode();

        // 1.发送验证码
        ResultEntity<String> stringResultEntity = CrowdUtil.sendCodeByShortMessage(host, path, method, appcode, phoneNum);

        // 2.判断短信发送结果
        String result = stringResultEntity.getResult();
        if(ResultEntity.SUCCESS.equals(result)){
        // 3.如果发送成功，则将验证码存入Redis
            // 3.1获取验证码
            String code = stringResultEntity.getData();
            // 3.2 拼接一个用于在Redis中存储数据的key
            String key = CrowdConstant.REDIS_CODE_PREFIX + phoneNum;
            // 3.3将验证码存入到redis中，远程调用RedisRemoteService
            ResultEntity resultEntity = redisRemoteService.setRedisKeyValueWithTimeoutRemote(key, code, 50, TimeUnit.MINUTES);

            // 3.4 判断存入redis是否成功
            String redisResult = resultEntity.getResult();

            if (ResultEntity.SUCCESS.equals(resultEntity.getResult())){
                // 存入成功，返回成功
                return ResultEntity.successWithOutData();
            } else {
                // 存入失败，返回redis返回的ResultEntity
                return resultEntity;
            }

        }else {
        // 4.验证码发送失败,直接返回ResultEntity
            return stringResultEntity;

        }

    }



    /**
     *      处理用户登录的请求
     * @param loginAcct
     * @param loginPswd
     * @param modelMap
     * @param session
     * @return
     */
    @RequestMapping("/auth/do/member/login.html")
    public String doMemberLogin(
            // 登录的密码
            @RequestParam("loginAcct") String loginAcct,
            // 登录的账号
            @RequestParam("loginPswd") String loginPswd,
            // 需要传输数据到域对象中
            ModelMap modelMap,
            // 传输域对象
            HttpSession session
            ){
        // 1.远程调用MySQLRemoteService中提供的方法，来查询当前用户是都存在
        ResultEntity<MemberPO> memberPOResultEntity = mySQLRemoteService.getMemberPOByLoginAcctRemote(loginAcct);

        // 2.判断查询结果是否正确
        String result = memberPOResultEntity.getResult();
        if(ResultEntity.FAILED.equals(result)){
            // 2.1失败返回到登录页面，在页面尚显示错误信息
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE,memberPOResultEntity.getMessage());
            return "member-login";
        }

        // 3.查询成功取出用户
        MemberPO memberPO = memberPOResultEntity.getData();
        // 3.1 判断用户是由有效
        if(memberPO == null){
            // 3.2用户不存在返回到登录页面
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE,CrowdConstant.MESSAGE_LOGIN_FAILED);
            return "member-login";
        }

        // 4.查询用户正确后取出数据库中的密码(已经加密了的)
        String userpswd = memberPO.getUserpswd();
        // 4.1判断密码是否正确
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        boolean matches = passwordEncoder.matches(userpswd, loginPswd);
        if(!matches){
            // 4.2密码不正确,返回登录页面
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE,CrowdConstant.MESSAGE_LOGIN_FAILED);
            return "member-login";
        }

        // 5.密码匹配，则通过一个LoginMemberVO对象，存入需要在session域通信的用户信息（这样只在session域放一些相对不私秘的信息，保护用户隐私）
        LoginMemberVO loginMemberVO = new LoginMemberVO(memberPO.getId(),memberPO.getUsername(),memberPO.getEmail());

        // 6.将loginMemberVO保存在session中(保存在session的原因也就是这个实体类需要在多个微服务尚传输)
        session.setAttribute(CrowdConstant.ATTR_NAME_LOGIN_MEMBER,loginMemberVO);

        // 7.重定向到登录以后的主页面(重定向的原因是避免重复提交表单)
        return "redirect:http://localhost/auth/to/member/center/page.html";
    }


    /**
     *    处理退出的请求
     * @param session
     * @return
     */
    @RequestMapping("/auth/do/member/logout.html")
    public String doLogout(HttpSession session){

        // 1.清除session
        session.invalidate();

        // 2.重定向到主页面
        return "redirect:http://localhost/";
    }


}
