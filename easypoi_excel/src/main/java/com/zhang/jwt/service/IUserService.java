package com.zhang.jwt.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhang.jwt.dom.User;

import java.util.List;
import java.util.Map;

public interface IUserService extends IService<User> {


    List<Map<String, Object>> getUsers();

    int addUser(User user);

    List<User> getList();

    boolean checkUsername(String name);
}
