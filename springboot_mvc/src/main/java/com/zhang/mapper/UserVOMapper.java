package com.zhang.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zhang.domain.User;
import com.zhang.domain.vo.UserVO;
import com.zhang.query.UserQuery;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Component
public interface UserVOMapper extends BaseMapper<UserVO> {

    IPage<UserVO> selectUserVOByPage(IPage<User> userIPage, @Param("query") UserQuery query);
}
