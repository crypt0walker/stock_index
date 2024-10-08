package com.async.stock.vo.resp;

import lombok.Data;

@Data
public class PermissionRespNodeTreeVo {
    /**
     * 权限ID
     */
    private String id;
    /**
     * 菜单名称
     */
    private String title;
    /**
     * 菜单等级 1.目录 2.菜单 3.按钮
     */
    private int level;
}
