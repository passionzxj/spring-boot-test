package com.zhang.jwt;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhang.jwt.domain.User;
import com.zhang.jwt.domain.dto.UserDto;
import com.zhang.jwt.domain.vo.UserVO;
import com.zhang.jwt.mapper.UserMapper;
import com.zhang.jwt.service.IUserService;
import com.zhang.jwt.query.UserQuery;
import com.zhang.jwt.util.RedisKey;
import com.zhang.jwt.util.RedisUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserTest {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private IUserService userService;

    @Autowired
    RedisUtil redisUtil;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;;


    @Test
    public void testRedis() throws Exception{
//        boolean count = redisUtil.set("count", 111);
//        System.out.println("count:"+count);
        String key= RedisKey.ARTICLE_VIEWCOUNT_CODE+3;
        boolean count = redisUtil.hset(RedisKey.ARTICLE_VIEWCOUNT_KEY, key,2);
        System.out.println("count:"+count);
        Map<Object,Object> viewCountItem=redisUtil.hmget(RedisKey.ARTICLE_VIEWCOUNT_KEY);
        System.out.println(viewCountItem);
        redisUtil.remove(RedisKey.ARTICLE_VIEWCOUNT_KEY);
    }

    @Test
    public void test() throws Exception {
//        List<User> users = userMapper.selectList(null);
//        users.forEach(e -> System.out.println(e));
        UserQuery userQuery = new UserQuery();
        userQuery.setPageSize(3L);
        Page<User> userPage = new Page<>(userQuery.getPageNum(), userQuery.getPageSize());
        IPage<User> userIPage = userMapper.selectPage(userPage, null);
        System.out.println(userIPage.getTotal());
        userIPage.getRecords().forEach(e -> System.out.println(e));

    }

    @Test
    public void test111() throws Exception {
        UserQuery userQuery = new UserQuery();
        userQuery.setPageNum(2L);
        IPage<UserVO> byPage = userService.getByPage(userQuery);
        byPage.getRecords().forEach(e -> System.out.println("======" + e));
    }

    @Test
    public void testAdd() throws Exception{
        for (int i = 0; i < 5; i++) {
            User user = new User();
            user.setName("鼓捣鼓捣"+i);
            user.setAge(22+i);
            user.setEmail("211573438@qq.com");
            user.setDeptId(2L);
            userService.addUser(user);

        }
    }

    @Test
    public void testStream() throws Exception{
        List<UserDto> userDtos = userService.do2Dto();
        userDtos.forEach(System.out::println);
    }
}
