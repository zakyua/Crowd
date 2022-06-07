package com.atguigu.crowd.constant;

import javax.crypto.interfaces.PBEKey;

/**
 *  保存常量
 * @author ChenCheng
 * @create 2022-05-11 17:51
 */
public class CrowdConstant {


    public static final String MESSAGE_LOGIN_FAILED = "抱歉！账号密码错误！请重新输入！";
    public static final String MESSAGE_LOGIN_ACCT_ALREADY_IN_USE = "抱歉！这个账号已经被使用了！";
    public static final String MESSAGE_ACCESS_FORBIDEN = "请登录以后再访问！";
    public static final String MESSAGE_STRING_INVALIDATE = "字符串不合法！请不要传入空字符串！";
    public static final String MESSAGE_SYSTEM_ERROR_LOGIN_NOT_UNIQUE = "系统错误：登录账号不唯一！";
    public static final String MESSAGE_ACCESS_DENIED = "抱歉！您不能访问这个资源！";
    public static final String MESSAGE_CODE_NOT_EXIST = "验证码无效！请检查是否输入了正确的手机号";
    public static final String MESSAGE_CODE_INVALID = "验证码输入错误";
    public static final String MESSAGE_HEADER_PIC_EMPTY = "头图不能为空";
    public static final String MESSAGE_HEADER_PIC_UPLOAD_FAILED = "头图上传失败";
    public static final String MESSAGE_DETAIL_PIC_UPLOAD_FAILED = "项目图片上传失败";
    public static final String MESSAGE_DETAIL_PIC_EMPTY = "项目图片为空";
    public static final String MESSAGE_RETURN_PIC_EMPTY = "项目说明图片为空";
    public static final String MESSAGE_TEMPLE_PROJECT_MISSING = "临时ProjectVO对象未找到！";




    public static final String REDIS_CODE_PREFIX = "REDIS_CODE_PREFIX_";
    public static final String ATTR_NAME_EXCEPTION = "exception";
    public static final String ATTR_NAME_LOGIN_ADMIN = "loginAdmin";
    public static final String ATTR_NAME_PAGE_INFO = "pageInfo";
    public static final String ATTR_NAME_LOGIN_MEMBER = "loginMember";
    public static final String ATTR_NAME_ASSIGNED_ROLE = "assignedRoleList";
    public static final  String ATTR_NAME_UNASSIGNED_ROLE = "unAssignedRoleList";
    public static final String ATTR_NAME_MESSAGE = "message";
    public static final String ATTR_NAME_PORTAL_TYPE_LIST = "portal_type_list";
    public static final String ATTR_NAME_DETAIL_PROJECT = "detailProjectVO";
    public static final String ATTR_NAME_TEMPLE_PROJECT = "templeProject";




}
