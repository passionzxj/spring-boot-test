package com.zhang.controller;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.result.ExcelImportResult;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhang.config.ExcelUtil;
import com.zhang.config.UserExcelVerifyHandler;
import com.zhang.dom.Department;
import com.zhang.dom.Person;
import com.zhang.dom.User;
import com.zhang.dom.UserVo;
import com.zhang.service.IDepartmentService;
import com.zhang.service.IUserService;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("poi")
public class POIController {

    @Autowired
    private IUserService userService;
    @Autowired
    private IDepartmentService departmentService;
    @Autowired
    private UserExcelVerifyHandler userExcelVerifyHandler;

    @RequestMapping("export")
    public void export(HttpServletResponse response) {

        //模拟从数据库获取需要导出的数据
        List<Person> personList = new ArrayList<>();
        Person person1 = new Person("路飞", "1", new Date());
        Person person2 = new Person("娜美", "2", DateUtils.addDays(new Date(), 3));
        Person person3 = new Person("索隆", "1", DateUtils.addDays(new Date(), 10));
        Person person4 = new Person("小狸猫", "1", DateUtils.addDays(new Date(), -10));
        personList.add(person1);
        personList.add(person2);
        personList.add(person3);
        personList.add(person4);

        //导出操作
        ExcelUtil.exportExcel(personList, "花名册", "草帽一伙", Person.class, "海贼王.xls", response);
    }

    @RequestMapping("importExcel")
    public void importExcel() {
        String filePath = "F:\\海贼王.xls";
        //解析excel，
        List<Person> personList = ExcelUtil.importExcel(filePath, 1, 1, Person.class);
//        也可以使用MultipartFile,使用 FileUtil.importExcel(MultipartFile file, Integer titleRows, Integer headerRows, Class<T> pojoClass)导入
//        FileUtil.importExcel(MultipartFile file, Integer titleRows, Integer headerRows, Class<T> pojoClass)
        System.out.println("导入数据一共【" + personList.size() + "】行.");
        //TODO 保存数据库
    }


    @RequestMapping("user/export")
    public void getUserExcel(HttpServletResponse response) {
        List<User> userList = userService.getList();
        List<UserVo> userVos = new ArrayList<>();
        for (User user : userList) {
            UserVo userVo = new UserVo();
            BeanUtils.copyProperties(user, userVo);
            userVo.setDeptName(departmentService.getById(userVo.getDeptId()).getName());
            userVo.setBirthday(DateUtil.formatDate(user.getBirthday()));
            userVos.add(userVo);
        }
        ExcelUtil.exportExcel(userVos, "员工名单", "sheet1", UserVo.class, "员工.xls", response);
    }

    @RequestMapping("user/importExcel")
    public void importUserExcel() {
        String filePath = "F:\\员工.xls";
        List<UserVo> userVoList = ExcelUtil.importExcel(filePath, 1, 1, UserVo.class);
        System.out.println("导入数据一共【" + userVoList.size() + "】行");

        //TODO 保存数据库


        //TODO 验证无法生效   需要导包自定义配置验证类

        List<User> userList = new ArrayList<>();
        for (UserVo userVo : userVoList) {
            User user = new User();
            BeanUtils.copyProperties(userVo, user);
            Department department = departmentService.getBaseMapper().selectOne(new QueryWrapper<Department>().eq("name", userVo.getDeptName()));
            user.setDeptId(department.getId());
            user.setCreateTime(LocalDateTime.now());
            user.setBirthday(DateUtil.parseDate(userVo.getBirthday()));
            userList.add(user);
        }
        userList.forEach(System.out::println);
        userService.saveBatch(userList);

    }


    public String empImport(MultipartFile empFile, HttpServletResponse response) throws Exception {
        //获得输入的流
        InputStream inputStream = empFile.getInputStream();
        //获得基本的参数配置
        ImportParams params = new ImportParams();
        //params.setTitleRows(1);
        //params.setHeadRows(1);
        //设置开启参数的验证
        params.setNeedVerfiy(true);
        //参数开启自定义的验证
        params.setVerifyHanlder(userExcelVerifyHandler);
        //得到excel中User对象的集合
        ExcelImportResult<User> result = ExcelImportUtil.importExcelMore(inputStream, User.class, params);
        //getList:获取到引入成功的数据,getFailList:获取到引入失败的数据
        List<User> list = result.getList();
        list.forEach(e -> {
//            e.setPassword("123");//设置初始密码
//            //excel中的部门名称查出部门对象,放入到员工对象中,在持久化保存到数据库
//            if (e.getDepartment() != null) {
//                Department dept = departmentService.findByName(e.getDepartment().getName());
//                e.setDepartment(dept);
//            }
//            employeeService.save(e);
        });
        System.out.println("----------------------------------------------");
        //如果导入失败的文件应该从前台导出excel,并告知失败的原因
        if (result.isVerfiyFail()) {
            //错误的文件
            Workbook wb = result.getFailWorkbook();
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"); //mime类型
            response.setHeader("Content-disposition", "attachment;filename=error.xlsx");
            response.setHeader("Pragma", "No-cache");//设置不要缓存
            OutputStream ouputStream = response.getOutputStream();
            wb.write(ouputStream);
            ouputStream.flush();
            ouputStream.close();
        }
        return "import";
    }
}
