package com.atguigu.crowd.service.api;

import com.atguigu.crowd.entity.Auth;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author ChenCheng
 * @create 2022-05-22 11:12
 */
public interface AuthService {


    List<Auth> getAll();


    List<Integer> getAuthByRoleId(Integer roleId);

    void saveRoleAuthRelathinship(Map<String, List<Integer>> map);

    List<String> getAssignedAuthNameByAdminId(Integer adminId);

    List<String> getAuthByRoleIdList(ArrayList<Integer> roleIdList);
}
