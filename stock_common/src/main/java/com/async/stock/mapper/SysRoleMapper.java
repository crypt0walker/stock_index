package com.async.stock.mapper;

import com.async.stock.pojo.entity.SysRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author 16232
* @description 针对表【sys_role(角色表)】的数据库操作Mapper
* @createDate 2024-09-22 22:21:20
* @Entity com.async.stock.pojo.entity.SysRole
*/
public interface SysRoleMapper {

    int deleteByPrimaryKey(Long id);

    int insert(SysRole record);

    int insertSelective(SysRole record);

    SysRole selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysRole record);

    int updateByPrimaryKey(SysRole record);
    /**
     * 查所有存在的角色信息
     * @return
     */
    List<SysRole> findAllInfo();

    /**
     * 查询所有角色信息
     * @return
     */
    List<SysRole> findAllRoleInfo();


    /**
     * 根据角色ID查询所有角色信息
     * @param id
     * @return
     */
    List<SysRole> getRoleByUserId(@Param("id") Long id);
}
