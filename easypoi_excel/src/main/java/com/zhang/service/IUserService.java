package com.zhang.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhang.dom.User;

import java.util.List;
import java.util.Map;

public interface IUserService extends IService<User> {


    List<Map<String, Object>> getUsers();

    int addUser(User user);

    List<User> getList();

    boolean checkUsername(String name);
}
