package com.async.stock.service;

import com.async.stock.vo.req.UserAddReqVo;
import com.async.stock.vo.req.UserEditReqVO;
import com.async.stock.vo.req.UserOwnRoleReqVo;
import com.async.stock.vo.req.UserPageReqVo;
import com.async.stock.vo.resp.PageResult;
import com.async.stock.vo.resp.R;
import com.async.stock.vo.resp.UserInfoRespVo;
import com.async.stock.vo.resp.UserOwnRoleRespVo;

import java.util.List;

/**
 * @author by async
 * @Date 2021/12/21
 * @Description 定义用户信息服务接口
 */
public interface UserService4Sys {
    /**
     * 多条件综合查询用户分页信息，条件包含：分页信息 用户创建日期范围
     * @param userPageReqVo
     * @return
     */
    R<PageResult> pageUsers(UserPageReqVo userPageReqVo);

    /**
     * 添加用户信息
     * @param vo
     * @return
     */
    R<String> addUser(UserAddReqVo vo);

    /**
     * 更新用户信息
     * @param vo
     * @return
     */
    R<String> updateUser(UserEditReqVO vo);

    /**
     * 获取用户具有的角色信息，以及所有角色信息
     * @param userId
     * @return
     */
    R<UserOwnRoleRespVo> getUserOwnRole(String userId);

    /**
     * 更新用户角色信息
     * @param vo
     * @return
     */
    R<String> updateUserOwnRoles(UserOwnRoleReqVo vo);

    /**
     * 批量删除用户信息
     * @param userIds
     * @return
     */
    R<String> deleteUsers(List<String> userIds);

    /**
     * 根据用户id查询用户详情信息
     * @param id 用户id
     * @return
     */
    R<UserInfoRespVo> getUserInfo(String id);
}
