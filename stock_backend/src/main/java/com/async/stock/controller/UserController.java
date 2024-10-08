package com.async.stock.controller;

import com.async.stock.pojo.entity.SysUser;
import com.async.stock.service.UserService;
import com.async.stock.vo.req.LoginReqVo;
import com.async.stock.vo.resp.LoginRespVo;
import com.async.stock.vo.resp.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

//swagger对类的注解，描述类作用
@Api(value = "用户认证相关接口定义",tags = "用户功能-用户登录功能")
@RestController // @RestController 表示该类的所有方法返回的数据都会直接写入 HTTP 响应体中，而不是返回视图。
@RequestMapping("/api") // 基本路径 "/api"，用于区分 API 访问路径
//更具用户名查询用户信息
public class UserController {

    @Autowired // 自动注入 UserService，该服务层用于处理用户相关业务逻辑
    private UserService userService;
    //swagger注解，作用在方法上，描述该方法作用的功能
    @ApiOperation(value = "根据用户名查询用户信息",notes = "用户信息查询",response = SysUser.class)
    //swagger注解，作用在方法上，描述该方法参数信息，参数类型（数据提交方式）、数据类型、是否必要等
    @ApiImplicitParam(paramType = "path",name = "username",value = "用户名",required = true)
    @GetMapping(value = "/user/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    public SysUser getUserByUserName(@PathVariable("username") String name) {
        return userService.getUserByUserName(name);
    }

    /**
     * 用户登录功能实现。
     *
     * @param vo 封装的登录请求数据，LoginReqVo 包含用户名和密码。
     * @return 返回 R 封装的登录响应对象 (LoginRespVo)，其中包含用户登录后的相关信息。
     */
    @ApiOperation(value = "用户登录功能",notes = "用户登录",response = R.class)
    @PostMapping("/login") // POST 请求方式，路径为 "/api/login"。前端会向该接口发送登录请求。
    public R<LoginRespVo> login(@RequestBody LoginReqVo vo) {
        // 调用 Service 层的 login 方法，处理登录逻辑，返回封装的响应结果。
        R<LoginRespVo> r = this.userService.login(vo);
        return r;
    }

    /**
     * 生成登录校验码的访问接口
     * @return
     */
    @ApiOperation(value = "验证码生成功能",response = R.class)
    @GetMapping("/captcha")
    public R<Map> getCaptchaCode(){
        return userService.getCaptchaCode();
    }

}
