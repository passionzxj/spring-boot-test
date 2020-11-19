package com.zhang.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.zhang.domain.User;
import com.zhang.domain.dto.UserDto;
import com.zhang.domain.vo.UserVO;
import com.zhang.query.UserQuery;

import java.util.List;
import java.util.Map;

public interface IUserService extends IService<User> {

    IPage<UserVO> getByPage(UserQuery query);

    PageInfo findVOByPage(UserQuery query);

    List<Map<String, Object>> getUsers();

    void addUser(User user);

    List<User> getList();

    PageInfo<UserVO> getPageInfoList(QueryWrapper<User> queryWrapper, int pageNum, int pageSize);

    IPage<UserVO> getPageList1(QueryWrapper<User> queryWrapper, int pageNum, int pageSize);

    IPage<UserVO> getPageList2(QueryWrapper<User> queryWrapper, int pageNum, int pageSize);

    List<UserDto> do2Dto();

    IPage<UserVO> getPageList3(int pageNum, int pageSize);
}
