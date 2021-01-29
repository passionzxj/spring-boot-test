package com.zhang.jwt.controller;

import com.zhang.jwt.conf.AjaxResult;
import com.zhang.jwt.conf.AuthorizationUser;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("")
public class BizController {

    @PostMapping("add/money")
    public AjaxResult test(@RequestBody AuthorizationUser authorizationUser){
        return AjaxResult.success(authorizationUser);
    }

}
