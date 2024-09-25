package com.async.stock.service;


import com.async.stock.pojo.entity.SysUser;
import com.async.stock.vo.req.LoginReqVo;
import com.async.stock.vo.resp.LoginRespVo;
import com.async.stock.vo.resp.R;

import java.util.Map;

/**
 * @author by async
 * @Date 2024/9/22
 * @Description 定义用户服务接口
 */
public interface UserService {

    /**
     * 根据用户查询用户信息
     * @param userName 用户名称
     * @return
     */
    SysUser getUserByUserName(String userName);
    /**
     * 用户登录功能实现
     * @param vo
     * @return
     */
    R<LoginRespVo> login(LoginReqVo vo);

    /**
     * 登录校验码生成服务方法
     * @return
     */
    R<Map> getCaptchaCode();
}
