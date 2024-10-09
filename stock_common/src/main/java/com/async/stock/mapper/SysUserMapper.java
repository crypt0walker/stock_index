package com.async.stock.mapper;

import com.async.stock.pojo.domain.UserPageListInfoDomain;
import com.async.stock.pojo.entity.SysUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author 16232
* @description 针对表【sys_user(用户表)】的数据库操作Mapper
* @createDate 2024-09-22 22:21:20
* @Entity com.async.stock.pojo.entity.SysUser
*/
public interface SysUserMapper {

    int deleteByPrimaryKey(Long id);

    int insert(SysUser record);

    int insertSelective(SysUser record);

    SysUser selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysUser record);

    int updateByPrimaryKey(SysUser record);

    SysUser findUserInfoByUserName(@Param("username") String username);

    /**
     * 根据用户名查询用户信息。
     *
     * @param username 用户名
     * @return 返回用户对象
     */
    SysUser findByUserName(@Param("username") String username);
    //根据名字查询用户
    SysUser findUserInfoByName(@Param("userName") String userName);


    List<UserPageListInfoDomain> findUserAllInfoByPage(@Param("userName") String username, @Param("nickName") String nickName, @Param("startTime") String startTime, @Param("endTime") String endTime);

    /**
     * 逻辑删除用户信息，就是将delete变成为0
     * @param ids
     * @return
     */
    int updateStatusForDelete(@Param("ids") List<Long> ids);
}
