package com.async.stock.mapper;

import com.async.stock.pojo.entity.SysRolePermission;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
* @author 16232
* @description 针对表【sys_role_permission(角色权限表)】的数据库操作Mapper
* @createDate 2024-09-22 22:21:20
* @Entity com.async.stock.pojo.entity.SysRolePermission
*/
public interface SysRolePermissionMapper {

    int deleteByPrimaryKey(Long id);

    int insert(SysRolePermission record);

    int insertSelective(SysRolePermission record);

    SysRolePermission selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysRolePermission record);

    int updateByPrimaryKey(SysRolePermission record);
    /**
     * 批量添加角色权限中间表数据
     * @param list
     * @return
     */
    int addRolePermissionInfo(@Param("list") List<SysRolePermission> list);

    /**
     * 根据角色id获取权限id
     * @param roleId
     * @return
     */
    Set<String> getPermissionIdByRoleId(@Param("roleId") String roleId);

    int deleteByRoleId(@Param("id") Long id);

    /**
     * 根据权限id删除角色权限表
     * @param permissionId
     */
    void deleteByPermissionId(@Param("permissionId") Long permissionId);
}
