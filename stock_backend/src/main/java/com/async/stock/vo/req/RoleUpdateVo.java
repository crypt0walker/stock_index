package com.async.stock.vo.req;

import lombok.Data;

import java.util.List;

/**
 * @author by itheima
 * @Date 2021/12/22
 * @Description 角色添加VO
 */
@Data
public class RoleUpdateVo {

    /**
     * 角色ID
     */
    private String id;

    /**
     * 角色名称
     */
    private String name;
    /**
     * 角色描述
     */
    private String description;

    /**
     * 权限ID集合
     */
    private List<String> permissionsIds;
}
