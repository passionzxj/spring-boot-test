package com.code.gen.out.web.controller;

import com.code.gen.out.annotation.OperLog;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user")
public class UserController {

    @GetMapping("all")
    @OperLog(operModule = "系统管理-查询用户",operType = "查询用户", operDesc = "查询全部用户功能")
    public ResponseEntity getAll(){
        return ResponseEntity.ok("进来了");
    }
}
