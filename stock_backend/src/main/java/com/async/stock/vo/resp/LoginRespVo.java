package com.async.stock.vo.resp;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author by async
 * @Date 2024/9/23
 * @Description 登录后响应前端的 VO（值对象），封装了用户的相关信息，用于后端返回给前端。
 */
@Data // Lombok 自动生成 getter, setter, toString, equals, hashCode, 全参构造方法等
@NoArgsConstructor // Lombok 自动生成无参构造函数
@AllArgsConstructor // Lombok 自动生成全参构造函数
@Builder // Lombok 生成 Builder 模式，允许链式调用来创建对象
@ApiModel(description = "用户基本信息")
public class LoginRespVo {

    /**
     * 用户ID
     *
     * 使用 Jackson 的 @JsonSerialize 注解将 Long 类型的 ID 转换为 String 类型，
     * 解决前端 JS 处理大数值时的精度问题。前端的 number 类型无法精确表示较大的 Long 型数字，
     * 因此需要将其转换为字符串。
     */
    @ApiModelProperty(value = "主键id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 用户电话，返回给前端展示。
     */
    @ApiModelProperty(value = "电话号")
    private String phone;

    /**
     * 用户名，用于标识用户登录名或者展示。
     */
    @ApiModelProperty(value = "用户名")
    private String username;

    /**
     * 用户昵称，用于前端展示用户的个性化名称。
     */
    @ApiModelProperty(value = "昵称")
    private String nickName;
}
