package com.zhang.jwt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zhang.jwt.domain.User;
import com.zhang.jwt.domain.vo.UserVO;
import com.zhang.jwt.query.UserQuery;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Component
public interface UserVOMapper extends BaseMapper<UserVO> {

    IPage<UserVO> selectUserVOByPage(IPage<User> userIPage, @Param("query") UserQuery query);
}
