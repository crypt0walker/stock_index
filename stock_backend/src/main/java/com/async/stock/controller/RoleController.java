//package com.async.stock.controller;
//
//import com.async.stock.service.RoleService;
//import com.async.stock.vo.req.RoleAddVo;
//import com.async.stock.vo.req.RolePageReqVo;
//import com.async.stock.vo.req.RoleUpdateVo;
//import com.async.stock.vo.resp.PageResult;
//import com.async.stock.vo.resp.R;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.Set;
//
///**
// * @author by itheima
// * @Date 2021/12/22
// * @Description 角色处理器
// */
//@RestController
//@RequestMapping("/api")
//public class RoleController {
//
//    @Autowired
//    private RoleService roleService;
//
//    /**
//     * 分页查询当前角色信息
//     * @param vo
//     * @return
//     */
//    @PostMapping("/roles")
//    public R<PageResult> quryPageRole(@RequestBody RolePageReqVo vo){
//        return roleService.quryPageRole(vo);
//    }
//
//    /**
//     * 添加角色和角色关联权限
//     * @param vo
//     * @return
//     */
//    @PostMapping("/role")
//    public R<String> addRoleWithPermissions(@RequestBody RoleAddVo vo){
//        return roleService.addRoleWithPermissions(vo);
//    }
//
//    /**
//     * 根据角色id查找对应的权限id集合
//     * @param roleId
//     * @return
//     */
//    @GetMapping("/role/{roleId}")
//    public R<Set<String>>  getPermissionIdsByRoleId(@PathVariable("roleId") Long roleId){
//        return roleService.getPermissionIdsByRoleId(roleId);
//    }
//
//    /**
//     * 更新角色信息，包含角色关联的权限信息
//     * @param vo
//     * @return
//     */
//    @PutMapping("/role")
//    public R<String> updateRoleWithPermissions(@RequestBody RoleUpdateVo vo){
//        return roleService.updateRoleWithPermissions(vo);
//    }
//
//    /**
//     * 根据角色id删除角色信息
//     * @param roleId
//     * @return
//     */
//    @DeleteMapping("/role/{roleId}")
//    public R<String> deleteRole(@PathVariable("roleId")String roleId){
//        return roleService.deleteRoleById(roleId);
//    }
//
//    /**
//     * 更新用户的状态信息
//     * @param roleId 角色id
//     * @param status 状态 1.正常 0：禁用
//     * @return
//     */
//    @PostMapping("/role/{roleId}/{status}")
//    public R<String> updateRoleStatus(@PathVariable("roleId") String roleId,@PathVariable("status") Integer status){
//        return roleService.updateRoleStatus(roleId,status);
//    }
//
//
//}
