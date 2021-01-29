package com.zhang.jwt.controller;


import com.zhang.jwt.conf.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 授权、根据token获取用户详细信息
 */
@Slf4j
@RestController
@RequestMapping("auth")
public class AuthenticationController {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    @Qualifier("jwtUserDetailsService")
    private UserDetailsService userDetailsService;

    @Autowired
    StringRedisTemplate stringRedisTemplate; // 字符串Redis实例

    /**
     * 登录授权
     *
     * @param authorizationUser
     * @return
     */
    @PostMapping(value = "login")

    public AjaxResult login(@Validated @RequestBody AuthorizationUser authorizationUser) {

        final JwtUser jwtUser = (JwtUser) userDetailsService.loadUserByUsername(authorizationUser.getUsername());

        if (jwtUser == null) {
            return AjaxResult.fail("用户不存在！");
        }


        if (!jwtUser.getPassword().equals(EncryptUtils.encryptPassword(authorizationUser.getPassword()))) {
            return AjaxResult.fail("密码错误");
            //throw new AccountExpiredException("密码错误");
        }

        if (!jwtUser.isEnabled()) {
            //throw new AccountExpiredException("账号已停用，请联系管理员");
            return AjaxResult.fail("账号已停用，请联系管理员");
        }

        // 生成令牌
        final String token = jwtTokenUtil.generateToken(jwtUser);
        //用户信息存入redis
        RedisUserUtil.saveOrUpdateUser(stringRedisTemplate, jwtUser, token);

        // 返回 token
        return AjaxResult.success(new AuthenticationInfo(token, jwtUser));
    }

    /**
     * 获取用户信息
     *
     * @return
     */
    @GetMapping(value = "info")
    public AjaxResult getUserInfo() {
        JwtUser jwtUser = (JwtUser) userDetailsService.loadUserByUsername(SecurityUtils.getAccount());
        //return ResponseEntity.ok(jwtUser);
        if (jwtUser == null) {
            return AjaxResult.fail("获取用户数据失败");
        }
        return AjaxResult.success(jwtUser);
    }

}
