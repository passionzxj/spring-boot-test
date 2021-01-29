package com.zhang.controller;

import com.zhang.conf.AjaxResult;
import com.zhang.conf.AuthorizationUser;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("")
public class BizController {

    @PostMapping("add/money")
    public AjaxResult test(@RequestBody AuthorizationUser authorizationUser){
        return AjaxResult.success(authorizationUser);
    }

}
