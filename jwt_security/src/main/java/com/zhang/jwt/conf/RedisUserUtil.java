package com.zhang.jwt.conf;

import com.alibaba.fastjson.JSON;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

public class RedisUserUtil {

    public static void saveOrUpdateUser(StringRedisTemplate stringRedisTemplate , JwtUser user, String token){
        String dataStr = JSON.toJSONString(user);
        ValueOperations<String,String> valueOperations = stringRedisTemplate.opsForValue();
        valueOperations.set(token,dataStr);
    }

    public static JwtUser getUser(StringRedisTemplate stringRedisTemplate,String token){
        ValueOperations<String,String> valueOperations = stringRedisTemplate.opsForValue();
        String dataStr = valueOperations.get(token);
        JwtUser user = JSON.parseObject(dataStr,JwtUser.class);
        return user;
    }

    public void remove(StringRedisTemplate stringRedisTemplate,String token){
        stringRedisTemplate.delete(token);
    }

}
