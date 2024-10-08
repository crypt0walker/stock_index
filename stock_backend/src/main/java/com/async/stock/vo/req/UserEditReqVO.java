package com.async.stock.vo.req;

import lombok.Data;

/**
 * @author by itheima
 * @Date 2021/12/21
 * @Description 更新用户基本信息vo
 */
@Data
public class UserEditReqVO {
    private String id;
    private String username;
    private String phone;
    private String email;
    private String nickName;
    private String realName;
    private Integer sex;
    private Integer createWhere;
    private Integer status;
}
