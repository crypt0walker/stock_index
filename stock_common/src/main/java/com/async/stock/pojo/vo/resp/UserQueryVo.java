package com.async.stock.pojo.vo.resp;

import com.async.stock.pojo.entity.SysUser;
import lombok.Data;

/**
 * @author by async
 * @Date 2021/12/21
 * @Description 查询用户信息封装，包含用户基本信息和
 * 创建人和更新人信息
 */
@Data
public class UserQueryVo extends SysUser {
       private String createUserName;
       private String updateUserName;
}
