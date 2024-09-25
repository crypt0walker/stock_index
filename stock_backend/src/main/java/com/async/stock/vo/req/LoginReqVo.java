package com.async.stock.vo.req;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
/**
 * @author : async
 * @date : 2024/9/23
 * @description : 登录时请求参数的封装 VO（值对象），用于接收前端发送给后端的登录请求数据。
 *
 * VO (Value Object) 是一种用于传输数据的对象，它不包含业务逻辑，通常用于表示层与服务层之间的数据交换。
 * 在此处，LoginReqVo 用于封装登录请求的用户名和密码。
 */
@Data // Lombok 自动生成 getter, setter, toString, equals, hashCode 等方法，简化代码
@ApiModel(description = "登录返回信息")
public class LoginReqVo {

    /**
     * 用户名
     *
     * 前端发送登录请求时填写的用户名，后端根据该字段验证用户身份。
     */
    @ApiModelProperty(value = "用户名")
    private String username;

    /**
     * 密码
     *
     * 前端发送登录请求时填写的密码，用于与后端数据库中保存的密码进行比对，验证用户身份。
     * 通常密码在传输前会通过加密或其他安全手段保护，以防止明文传输。
     */
    @ApiModelProperty(value = "密码")
    private String password;
    @ApiModelProperty(value = "验证码")
    private String code; // 如果使用验证码登录，可以解开这个字段，用于接收验证码
    @ApiModelProperty(value = "sessionId")
    private String sessionId; // 如果使用验证码登录，可以解开这个字段，用于接收验证码
    //private String rkey; // 用于安全验证的随机密钥，比如用于防止重放攻击

}
