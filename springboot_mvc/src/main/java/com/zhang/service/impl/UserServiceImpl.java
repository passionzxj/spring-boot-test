package com.zhang.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.NumberUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhang.config.exception.BizException;
import com.zhang.domain.Department;
import com.zhang.domain.User;
import com.zhang.domain.dto.UserDto;
import com.zhang.domain.vo.UserVO;
import com.zhang.mapper.DepartmentMapper;
import com.zhang.mapper.UserMapper;
import com.zhang.mapper.UserVOMapper;
import com.zhang.query.UserQuery;
import com.zhang.service.IUserService;
import ma.glasnost.orika.MapperFacade;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserVOMapper userVOMapper;

    @Autowired
    private DepartmentMapper departmentMapper;

    @Override
    public IPage<UserVO> getByPage(UserQuery query) {
        Page<User> userPage = new Page<>(query.getPageNum(), query.getPageSize());
        return userVOMapper.selectUserVOByPage(userPage, query);
    }

    @Override
    public PageInfo findVOByPage(UserQuery query) {
        PageHelper.startPage(query.getPageNum().intValue(), query.getPageSize().intValue(), "age");

//方式一
//        QueryWrapper<User> wrapper = new QueryWrapper<>();
//        if (!StringUtils.isEmpty(query.getName())) {
//            wrapper.like("name", query.getName());
//        }
//        wrapper.between("age", query.getMinAge(), query.getMaxAge());

//方式二
//        wrapper.lambda().like(!StringUtils.isBlank(query.getName()),User::getName,query.getName())
//                        .between(User::getAge, query.getMinAge(),query.getMaxAge());
//        lamda.like(!StringUtils.isBlank(query.getName()),User::getName,query.getName())
//                .between(User::getAge, query.getMinAge(),query.getMaxAge());
//方式三
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(!StringUtils.isBlank(query.getName()), User::getName, query.getName())
                .between(User::getAge, query.getMinAge(), query.getMaxAge());

        List<User> users = userMapper.selectList(wrapper);
        PageInfo pageInfo = new PageInfo<>(users);
        List<UserVO> userVoList = new ArrayList<>();
        users.forEach(e -> userVoList.add(convertDo2Vo(e)));
        pageInfo.setList(userVoList);
        return pageInfo;
    }

    /**
     * 三重分组-->>先根据部门分组,再性别,再年龄
     *
     * @return
     */
    @Override
    public List<Map<String, Object>> getUsers() {
        Map<Long, Map<String, Map<String, List<User>>>> map = userMapper.selectList(null)
                .stream()
                .sorted(Comparator.comparingInt(User::getAge))
                .collect(Collectors.groupingBy(User::getDeptId, Collectors.groupingBy(User::getSex, Collectors.groupingBy(a -> {
                    if (a.getAge() <= 35) {
                        return "青年";
                    } else if (a.getAge() <= 60) {
                        return "中年";
                    } else {
                        return "老年";
                    }
                }))));
        List<Map<String, Object>> result = new ArrayList<>();
        for (Map.Entry<Long, Map<String, Map<String, List<User>>>> userMap : map.entrySet()) {
            Map<String, Object> hashMap1 = new HashMap<>();
            Department department = Optional.ofNullable(departmentMapper.selectById(userMap.getKey()))
                    .orElseThrow(() -> new BizException("部门信息出错"));
            hashMap1.put("key", department.getName());
            List<Map<String, Object>> list = new ArrayList<>();
            for (Map.Entry<String, Map<String, List<User>>> entry : userMap.getValue().entrySet()) {
                Map<String, Object> hashMap2 = new HashMap<>();
                hashMap2.put("sex_key", entry.getKey());
                hashMap2.put("users_value", entry.getValue());
                list.add(hashMap2);
            }
            hashMap1.put("value", list);
            result.add(hashMap1);
        }
        return result;
    }


    /**
     * 分页一
     * 使用mybatis的内置插件  PageHelper
     *
     * @param queryWrapper
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public PageInfo<UserVO> getPageInfoList(QueryWrapper<User> queryWrapper, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        queryWrapper.orderByDesc("create_time");
        List<User> users = userMapper.selectList(queryWrapper);
        PageInfo pageInfo = new PageInfo(users);
        List<UserVO> voList = new ArrayList<>();
        for (User user : users) {
            UserVO userVO = new UserVO();
            BeanUtils.copyProperties(user, userVO);
            userVO.setDepartment(departmentMapper.selectById(user.getDeptId()));
            voList.add(userVO);
        }
        pageInfo.setList(voList);
        return pageInfo;
    }


    /**
     * 分页二
     * mybatisPlus默认分页插件IPage  的page()方法
     *
     * @param queryWrapper
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public IPage<UserVO> getPageList1(QueryWrapper<User> queryWrapper, int pageNum, int pageSize) {
        queryWrapper.orderByDesc("create_time");
        Page<User> page = new Page<>(pageNum, pageSize);
        IPage iPage = page(page, queryWrapper);
        List<UserVO> list = do2vo(iPage);
        iPage.setRecords(list);
        return iPage;
    }

    /**
     * 分页三
     * mybatisPlus默认分页插件IPage  写sql的方式
     *
     * @param queryWrapper
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public IPage<UserVO> getPageList2(QueryWrapper<User> queryWrapper, int pageNum, int pageSize) {
        Page<User> page = new Page<>(pageNum, pageSize);
        queryWrapper.orderByDesc("create_time");
        IPage iPage = userMapper.selectPageList(page, queryWrapper);
        List<UserVO> list = do2vo(iPage);
        iPage.setRecords(list);
        return iPage;
    }

    private UserVO convertDo2Vo(User user) {
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        userVO.setDepartment(departmentMapper.selectById(user.getDeptId()));
        return userVO;
    }

    private List<UserVO> do2vo(IPage iPage) {
        List<UserVO> list = new ArrayList<>();
        for (Object record : iPage.getRecords()) {
            User user = (User) record;
            UserVO userVO = new UserVO();
            BeanUtils.copyProperties(user, userVO);
            userVO.setDepartment(departmentMapper.selectById(user.getDeptId()));
            list.add(userVO);
        }
        return list;
    }

    @Override
    public void addUser(User user) {
        userMapper.insert(user);
    }

    @Override
    public List<User> getList() {
        return userMapper.selectList(null);
    }

    public static void main(String[] args) {
        String s = convertNum2Chinese(1);
        System.out.println(s);
//// 最简单的HTTP请求，可以自动通过header等信息判断编码，不区分HTTP和HTTPS
//        String result1 = HttpUtil.get("https://www.baidu.com");
//
//// 当无法识别页面编码的时候，可以自定义请求页面的编码
//        String result2 = HttpUtil.get("https://www.baidu.com", CharsetUtil.CHARSET_UTF_8);
//
////可以单独传入http参数，这样参数会自动做URL编码，拼接在URL中
//        HashMap<String, Object> paramMap = new HashMap<>();
//        paramMap.put("city", "北京");
//
//        String result3 = HttpUtil.get("https://www.baidu.com", paramMap);
//        System.out.println(result1 + "\n" + result2 + "\n" + result3);

        Integer[] integers = NumberUtil.generateBySet(1, 20, 6);
        int[] ints = NumberUtil.generateRandomNumber(1, 20, 6);
        System.out.println(Arrays.toString(integers));
        System.out.println(Arrays.toString(ints));


        System.out.println("-------------------------------------------");


        String fileName = "fsfs/fss/eewr/aerfg/dgfg.png";

        String suffixName = fileName.substring(fileName.lastIndexOf(".") + 1);

        System.out.println("suffixName:" + suffixName);


    }


    public static String convertNum2Chinese(int num) {
        return Convert.numberToChinese(num, false);
    }

    @Autowired
    public List<UserDto> do2Dto(){
        List<User> userList = userMapper.selectList(null);
        List<UserDto> dtoList = userList.stream()
                .map(e -> {
                    UserDto dto = new UserDto();
                    BeanUtils.copyProperties(e, dto);
                    return dto;
                })
                .collect(Collectors.toList());
        return dtoList;
    }

    @Override
    public IPage<UserVO> getPageList3(int pageNum, int pageSize) {
        Page<UserVO> userVOPage = new Page<>(pageNum,pageSize);
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        List<UserVO> userVOS = userMapper.selectPageList33(userVOPage,wrapper);

        userVOPage.setRecords(userVOS);
        return userVOPage;
    }


}
