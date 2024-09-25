package com.async.stock;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;

/**
 * @author : itheima
 * @date : 2022/9/20 9:37
 * @description :
 */
@SpringBootTest
public class TestRedisCache {
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * @desc 测试redis缓存
     */
    @Test
    public void testCache() {
        String sessionId="12345678";
        String checkCode="7788";
        //一般给key加业务主键：CK:12345678->7788 好处：keys CK*
        redisTemplate.opsForValue().set("CK:"+sessionId,checkCode,1, TimeUnit.MINUTES);
        //获取
        String code = (String) redisTemplate.opsForValue().get("CK:" + sessionId);
        System.out.println(code);
    }
}
