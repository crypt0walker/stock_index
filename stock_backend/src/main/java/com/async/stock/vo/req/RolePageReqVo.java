package com.async.stock.vo.req;

import lombok.Data;

/**
 * @author by itheima
 * @Date 2021/12/22
 * @Description 角色列表查询参数封装
 */
@Data
public class RolePageReqVo {
    /**
     * 当前页
     */
    private Integer pageNum=1;
    /**
     * 每页大小
     */
    private Integer pageSize=10;
}
