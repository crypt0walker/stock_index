package com.async.stock.mapper;

import com.async.stock.pojo.entity.SysLog;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author 16232
* @description 针对表【sys_log(系统日志)】的数据库操作Mapper
* @createDate 2024-09-22 22:21:20
* @Entity com.async.stock.pojo.entity.SysLog
*/
public interface SysLogMapper {

    int deleteByPrimaryKey(Long id);

    int insert(SysLog record);

    int insertSelective(SysLog record);

    SysLog selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysLog record);

    int updateByPrimaryKey(SysLog record);

    List<SysLog> findByCondition(@Param("username") String username, @Param("operation") String operation, @Param("startTime") String startTime, @Param("endTime") String endTime);

    /**
     * 根据角色id批量删除用户信息
     * @param logIds
     */
    void deleteBatchByLogIds(@Param("logIds") List<Long> logIds);
}
