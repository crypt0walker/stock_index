package com.async.stock.vo.req;

import lombok.Data;

/**
 * @author by itheima
 * @Date 2021/12/21
 * @Description 用户添加vo
 */
@Data
public class UserAddReqVo {
    /**
     * 账户名称
     */
  private String  username;
  private String  password;
  private String  phone;
  private String  email;
  private String  nickName;
  private String  realName;
    /**
     * 性别 1：男 0：女
     */
  private Integer  sex;
  private Integer  createWhere;
    /**
     * 账户装填 1.正常 2. 锁定
     */
  private Integer  status;
}
