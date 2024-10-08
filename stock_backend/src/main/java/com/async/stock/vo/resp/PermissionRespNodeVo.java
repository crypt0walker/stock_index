package com.async.stock.vo.resp;

import lombok.Data;

import java.util.List;

/**
 * @author async
 * @version 1.0
 */
@Data
public class PermissionRespNodeVo {

    private String id;

    private String title;
    /**
     * 角色图标
     */
    private String icon;
    /**
     * 路由地址
     */
    private String path;
    /**
     * 姓名
     */
    private String name;
    /**
     * 菜单树结构
     */
    private List<PermissionRespNodeVo> children;
}
