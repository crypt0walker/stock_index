//package com.async.stock.controller;
//
//import com.async.stock.service.UserService4Sys;
//import com.async.stock.utils.IdWorker;
//import com.async.stock.vo.req.UserAddReqVo;
//import com.async.stock.vo.req.UserEditReqVO;
//import com.async.stock.vo.req.UserOwnRoleReqVo;
//import com.async.stock.vo.req.UserPageReqVo;
//import com.async.stock.vo.resp.PageResult;
//import com.async.stock.vo.resp.R;
//import com.async.stock.vo.resp.UserInfoRespVo;
//import com.async.stock.vo.resp.UserOwnRoleRespVo;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
///**
// * @author by async
// * @Date 2021/12/21
// * @Description
// */
//@RestController
//@RequestMapping("/api")
//public class UserController4Sys {
//
//    /**
//     * 注入服务
//     */
//    @Autowired
//    private UserService4Sys userService;
//
//    @Autowired
//    private RedisTemplate redisTemplate;
//
//    @Autowired
//    private IdWorker idWorker;
//
//
//
//    /**
//     * 多条件综合查询用户分页信息，条件包含：分页信息 用户创建日期范围
//     * @param userPageReqVo
//     * @return
//     */
//    @PostMapping("/users")
//    public R<PageResult>  pageUsers(@RequestBody UserPageReqVo userPageReqVo){
//      return this.userService.pageUsers(userPageReqVo);
//    }
//
//    /**
//     * 添加用户信息
//     * @param vo
//     * @return
//     */
//    @PostMapping("/user")
//    public R<String> addUser(@RequestBody UserAddReqVo vo){
//        return this.userService.addUser(vo);
//    }
//
//    /**
//     * 更新用户信息
//     * @param vo
//     * @return
//     */
//    @PutMapping("/user")
//    @PreAuthorize("hasAuthority('sys:user:update')")//对应的接口添加访问的权限，其它接口方法自定填写
//    public R<String> updateUser(@RequestBody UserEditReqVO vo){
//        return this.userService.updateUser(vo);
//    }
//
//    /**
//     * 获取用户具有的角色信息，以及所有角色信息
//     * @param userId
//     * @return
//     */
//    @GetMapping("/user/roles/{userId}")
//    public R<UserOwnRoleRespVo> getUserOwnRole(@PathVariable("userId")String userId){
//        return this.userService.getUserOwnRole(userId);
//    }
//
//    /**
//     * 更新用户角色信息
//     * @param vo
//     * @return
//     */
//    @PutMapping("/user/roles")
//    public R<String> updateUserOwnRoles(@RequestBody UserOwnRoleReqVo vo){
//        return this.userService.updateUserOwnRoles(vo);
//    }
//
//    /**
//     * 批量删除用户信息
//     * delete请求可通过请求体携带数据
//     * @param userIds
//     * @return
//     */
//    @DeleteMapping("/user")
//    public R<String> deleteUsers(@RequestBody List<String> userIds){
//        return this.userService.deleteUsers(userIds);
//    }
//
//
//    /**
//     * 根据用户id查询用户信息
//     * @param id
//     * @return
//     */
//    @GetMapping("/user/info/{userId}")
//    public R<UserInfoRespVo> getUserInfo(@PathVariable("userId") String id){
//        return userService.getUserInfo(id);
//    }
//
//
//}
