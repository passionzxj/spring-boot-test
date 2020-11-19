package com.zhang.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhang.dom.User;
import com.zhang.mapper.UserMapper;
import com.zhang.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl  extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<Map<String, Object>> getUsers() {
        return null;
    }

    @Override
    public int addUser(User user) {
        return 0;
    }

    @Override
    public List<User> getList() {
        return userMapper.selectList(null);
    }

    @Override
    public boolean checkUsername(String name) {
        return false;
    }
}