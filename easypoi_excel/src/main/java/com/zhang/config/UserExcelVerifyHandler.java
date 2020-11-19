package com.zhang.config;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.entity.result.ExcelVerifyHanlderResult;
import cn.afterturn.easypoi.handler.inter.IExcelVerifyHandler;
import com.zhang.dom.User;
import com.zhang.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserExcelVerifyHandler implements IExcelVerifyHandler<User> {
    @Autowired
    private IUserService userService;
    @Override
    public ExcelVerifyHanlderResult verifyHandler(User user) {
        //设置初始化的验证值为true
        ExcelVerifyHanlderResult excelVerifyHandlerResult = new ExcelVerifyHanlderResult(true);
        if (!userService.checkUsername(user.getName())){
            excelVerifyHandlerResult.setSuccess(false);
            excelVerifyHandlerResult.setMsg("此用户名已经存在");
        }
        return excelVerifyHandlerResult;
    }

}