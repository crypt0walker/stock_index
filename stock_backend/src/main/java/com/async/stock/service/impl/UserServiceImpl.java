package com.async.stock.service.impl;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import com.async.stock.constant.StockConstant;
import com.async.stock.mapper.SysUserMapper;
import com.async.stock.pojo.entity.SysUser;
import com.async.stock.service.UserService;
import com.async.stock.utils.IdWorker;
import com.async.stock.vo.req.LoginReqVo;
import com.async.stock.vo.resp.LoginRespVo;
import com.async.stock.vo.resp.R;
import com.async.stock.vo.resp.ResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service("UserService")
@Slf4j
public class UserServiceImpl implements UserService {
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired // 注入 PasswordEncoder，用于处理密码加密和校验
    private PasswordEncoder passwordEncoder;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public SysUser getUserByUserName(String userName) {
        return sysUserMapper.findUserInfoByUserName(userName);
    }


    /**
     * 用户登录功能
     * @param reqVo
     * @return
     */
    /**
     * 用户登录功能
     * @param vo
     * @return
     */
    @Override
    public R<LoginRespVo> login(LoginReqVo vo) {
        //1.校验参数的合法性
        if (vo==null || StringUtils.isBlank(vo.getUsername()) || StringUtils.isBlank(vo.getPassword())) {
            return R.error(ResponseCode.DATA_ERROR);
        }
        //2.校验验证码和sessionId是否有效
        if (StringUtils.isBlank(vo.getCode()) || StringUtils.isBlank(vo.getSessionId())){
            return R.error(ResponseCode.DATA_ERROR);
        }
        //3.从redis中获取缓存的校验码
        String rCode= (String) redisTemplate.opsForValue().get(StockConstant.CHECK_PREFIX+vo.getSessionId());
        //判断获取的验证码是否存在，以及是否与输入的验证码相同
        if (StringUtils.isBlank(rCode) || ! rCode.equalsIgnoreCase(vo.getCode())) {
            //验证码输入有误
            return R.error(ResponseCode.CHECK_CODE_ERROR);
        }
        //4.根据账户名称去数据库查询获取用户信息
        SysUser dbUser = sysUserMapper.findByUserName(vo.getUsername());
        //5.判断数据库用户是否存在
        if (dbUser==null) {
            return R.error(ResponseCode.ACCOUNT_NOT_EXISTS);
        }
        //6.如果存在，则获取密文密码，然后传入的明文进行匹配,判断是否匹配成功
        if (!passwordEncoder.matches(vo.getPassword(),dbUser.getPassword())) {
            return R.error(ResponseCode.USERNAME_OR_PASSWORD_ERROR);
        }
//        测试
//        String pwd= vo.getPassword();
//        String encode = passwordEncoder.encode(pwd);

//        dbUser.getPassword()
//        if (!passwordEncoder.matches(vo.getPassword(),encode)) {
//            return R.error(ResponseCode.USERNAME_OR_PASSWORD_ERROR);
//        }
        //7.正常响应
        LoginRespVo respVo = new LoginRespVo();
        BeanUtils.copyProperties(dbUser,respVo);
        return R.ok(respVo);
    }
    /**
     * 获取校验码
     * @return
     */
    @Override
    public R<Map> getCaptchaCode() {
        //参数分别是宽、高、验证码长度、干扰线数量
        LineCaptcha captcha = CaptchaUtil.createLineCaptcha(250, 40, 4, 5);
        //设置背景颜色清灰
        captcha.setBackground(Color.lightGray);
        //自定义校验码生成方式
//        captcha.setGenerator(new CodeGenerator() {
//            @Override
//            public String generate() {
//                return RandomStringUtils.randomNumeric(4);
//            }
//            @Override
//            public boolean verify(String code, String userInputCode) {
//                return code.equalsIgnoreCase(userInputCode);
//            }
//        });
        //获取图片中的验证码，默认生成的校验码包含文字和数字，长度为4
        String checkCode = captcha.getCode();
        //需要加注解@Slf4j
        log.info("生成校验码:{}",checkCode);
        //生成sessionId
        String sessionId = String.valueOf(idWorker.nextId());
        //将sessionId和校验码保存在redis下，并设置缓存中数据存活时间一分钟
        //这里在验证码存入redis前加了一个常量值，可以更好进行命名空间管理，易于识别维护，提高安全性。
//        1. 命名空间管理：使用前缀可以为不同类型的数据创建命名空间，避免不同模块或功能之间的数据冲突。例如，如果多个功能模块都使用 Redis 存储数据，使用不同的前缀可以确保它们存储的键不会相互覆盖
//        2. 易于识别：前缀可以让开发者快速识别某个键的用途或来源，例如 StockConstant.CHECK_PREFIX 表示这是与验证码相关的键。这样在后续的开发和维护中，查找和理解数据变得更加直观。
//        3. 清理与维护：在清理或维护缓存数据时，前缀可以帮助开发者快速找到和删除特定类型的数据。例如，如果需要删除所有与验证码相关的缓存，可以通过前缀进行批量操作。
//        4. 提升安全性：如果不同的系统或模块共享同一个 Redis 实例，使用前缀可以减少因键名相同而引起的意外覆盖或错误访问，提高系统的安全性和稳定性。
        redisTemplate.opsForValue().set(StockConstant.CHECK_PREFIX +sessionId,checkCode,1, TimeUnit.MINUTES);
        //组装响应数据
        HashMap<String, String> info = new HashMap<>();
        info.put("sessionId",sessionId);
        info.put("imageData",captcha.getImageBase64());//获取base64格式的图片数据
        //设置响应数据格式
        return R.ok(info);
    }



}
