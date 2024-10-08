package com.async.stock.vo.resp;

import com.async.stock.pojo.entity.SysRole;
import lombok.Data;

import java.util.List;

/**
 * @author by async
 * @Date 2021/12/21
 * @Description 响应用户角色信息
 */
@Data
public class UserOwnRoleRespVo {
    /**
     * 用户用户的角色id集合
     */
    private List<String> ownRoleIds;
    /**
     * 所有角色集合
     */
    private List<SysRole> allRole;
}
