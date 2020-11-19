package com.zhang.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhang.domain.User;
import com.zhang.domain.vo.UserVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface UserMapper extends BaseMapper<User> {
    IPage<User> selectPageList(Page<User> page, @Param(Constants.WRAPPER) Wrapper query);

    List<UserVO> selectPageList33(Page<UserVO> userVOPage, QueryWrapper<User> wrapper);
}
