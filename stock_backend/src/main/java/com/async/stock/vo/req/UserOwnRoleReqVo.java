package com.async.stock.vo.req;

import lombok.Data;

import java.util.List;

/**
 * @author by itheima
 * @Date 2021/12/21
 * @Description 封装用户更新角色信息vo
 */
@Data
public class UserOwnRoleReqVo {

    private String userId;

    private List<String> roleIds;
}
