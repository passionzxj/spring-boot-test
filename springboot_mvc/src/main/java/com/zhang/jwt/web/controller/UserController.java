package com.zhang.jwt.web.controller;

import cn.hutool.core.io.IoUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.pagehelper.PageInfo;
import com.zhang.jwt.config.util.ConditionVo;
import com.zhang.jwt.config.util.QueryConditionDto;
import com.zhang.jwt.domain.Ability;
import com.zhang.jwt.domain.User;
import com.zhang.jwt.domain.vo.UserVO;
import com.zhang.jwt.service.IDepartmentService;
import com.zhang.jwt.query.UserQuery;
import com.zhang.jwt.service.IAbilityService;
import com.zhang.jwt.service.IUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cn.hutool.core.io.IoUtil.DEFAULT_BUFFER_SIZE;

@CrossOrigin
@RestController
@RequestMapping("/hello")
public class UserController {

    @Autowired
    private IUserService userService;
    @Autowired
    private IDepartmentService departmentService;
    @Autowired
    private IAbilityService abilityService;


    @GetMapping("download")
    public ResponseEntity<byte[]> download() throws IOException{
        ClassPathResource resource = new ClassPathResource("template/question_template.xml");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

        final String fileName = "试题模板.xml";
        headers.setContentDispositionFormData("attachment", java.net.URLEncoder.encode(fileName, "UTF-8"));

        try (InputStream input = resource.getInputStream();
             ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            IoUtil.copy(input, output, DEFAULT_BUFFER_SIZE);

            return new ResponseEntity<>(output.toByteArray(),headers, HttpStatus.CREATED);
        }
    }


    @PutMapping(value = "/save")
    public Map<String, Object> creatOrUpdateUser(@RequestBody UserVO userVO) {
        System.out.println(userVO.getId());
        User user = new User();
        Map<String, Object> result = new HashMap<>();
        try {
            user.setId(userVO.getId());
            user.setName(userVO.getName());
            user.setAge(userVO.getAge());
            user.setEmail(userVO.getEmail());
            user.setDeptId(userVO.getDepartment().getId());
            if (StringUtils.isBlank(user.getId())) {
                boolean addSave = userService.save(user);
                if (addSave) {
                    result.put("success", true);
                    result.put("message", "添加......");
                    return result;
                }
                result.put("success", false);
                return result;
            } else {
                System.out.println(user);
                boolean updateSave = userService.updateById(user);
                if (updateSave) {
                    result.put("success", true);
                    result.put("message", "修改......");
                    return result;
                }
                result.put("success", false);
                return result;
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
            return result;
        }
    }

    @PostMapping("findVOByPage")
    public Map<String, Object> getUsersPage1(@RequestBody UserQuery query) {
        Map<String, Object> result = new HashMap<>();
        try {
            PageInfo userVOIPage = userService.findVOByPage(query);
            result.put("success", true);
            result.put("total", userVOIPage.getTotal());
            result.put("data", userVOIPage.getList());
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
            return result;
        }
    }

    @PostMapping("getByPage")
    public Map<String, Object> getUsersPage2(@RequestBody UserQuery query) {
        Map<String, Object> result = new HashMap<>();
        try {
            IPage<UserVO> userVOIPage = userService.getByPage(query);
            result.put("success", true);
            result.put("total", userVOIPage.getTotal());
            result.put("data", userVOIPage.getRecords());
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
            return result;
        }
    }

    @DeleteMapping(value = "{id}")
    public Map<String, Object> deleteById(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean byId = userService.removeById(id);
            if (byId) {
                result.put("success", true);
                return result;
            }
            result.put("success", false);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
            return result;
        }
    }

    @GetMapping("group")
    public ResponseEntity getGroupBy() throws IOException {
        List<Map<String, Object>> users = userService.getUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("ability1")
    public ResponseEntity getAbility1(@RequestParam String abilityId) {
        List<Ability> tree = abilityService.findTree(abilityId);
        return ResponseEntity.ok(tree);
    }

    @GetMapping("ability2")
    public ResponseEntity getAbility2(@RequestParam Long pid) {
        List<Ability> tree = abilityService.findListTree(pid);
        return ResponseEntity.ok(tree);
    }


    /**
     * MyBatisPlus自带的分页插件  IPage
     *
     * @param pageNum
     * @param pageSize
     * @param listParams
     * @return
     */
    @PostMapping("ipage/list1")
    public ResponseEntity getIPageList1(@RequestParam(name = "pageNum", required = false, defaultValue = "1") int pageNum,
                                        @RequestParam(name = "pageSize", required = false, defaultValue = "20") int pageSize,
                                        @RequestBody List<ConditionVo> listParams) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        QueryWrapper<User> queryWrapper = QueryConditionDto.parseWhereSql222(listParams);
        IPage<UserVO> pageList = userService.getPageList1(queryWrapper, pageNum, pageSize);
        return ResponseEntity.ok(pageList);
    }

    /**
     * MyBatisPlus自带的分页插件  IPage
     *
     * @param pageNum
     * @param pageSize
     * @param conditionJson
     * @return
     */
    @PostMapping("ipage/list2")
    public ResponseEntity getIPageList2(@RequestParam(name = "pageNum", required = false, defaultValue = "1") int pageNum,
                                        @RequestParam(name = "pageSize", required = false, defaultValue = "20") int pageSize,
                                        @RequestParam(name = "condition", required = false) String conditionJson) {
        QueryWrapper<User> queryWrapper = QueryConditionDto.parseWhereSql(conditionJson);
        IPage<UserVO> pageList = userService.getPageList2(queryWrapper, pageNum, pageSize);
        return ResponseEntity.ok(pageList);
    }

    /**
     * MyBatis自带的分页插件  PageHelper
     *
     * @param pageNum
     * @param pageSize
     * @param conditionJson
     * @return
     */
    @PostMapping("pageinfo/list")
    public ResponseEntity getPageInfoList(@RequestParam(name = "pageNum", required = false, defaultValue = "1") int pageNum,
                                          @RequestParam(name = "pageSize", required = false, defaultValue = "20") int pageSize,
                                          @RequestParam(name = "condition", required = false) String conditionJson) {
        QueryWrapper<User> queryWrapper = QueryConditionDto.parseWhereSql(conditionJson);
        PageInfo<UserVO> pageInfoList = userService.getPageInfoList(queryWrapper, pageNum, pageSize);
        return ResponseEntity.ok(pageInfoList);
    }


    public static void main(String[] args) {
        String str = "ab2cdecfghcijkdldmn";
        String substring = str.substring(0, 6);
        System.out.println(substring);
        System.out.println(str.indexOf("c"));
        System.out.println(str.indexOf("c", 5));

    }

    @GetMapping("ipage/list3")
    public ResponseEntity getIPageList3(@RequestParam(name = "pageNum", required = false, defaultValue = "1") int pageNum,
                                        @RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize) {
        IPage<UserVO> pageList = userService.getPageList3(pageNum, pageSize);
        return ResponseEntity.ok(pageList);
    }
}
