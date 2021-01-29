package com.zhang.jwt.dom;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserVo {
    private String id;
    @Excel(name = "姓名", orderNum = "0")
    @NotNull(message = "导入员工的用户名必须存在")
    private String name;
    @Excel(name = "性别", replace = {"男_1", "女_2"}, orderNum = "1")
    private String sex;
    @Excel(name = "年龄", orderNum = "2")
    @Max(value = 80, message = "导入员工的年龄不能超过80岁")
    @Min(value = 20, message = "导入员工的年龄不能低于20岁")
    private Integer age;
    @Excel(name = "生日", exportFormat = "yyyy-MM-dd", orderNum = "3",width = 12)
    private String birthday;
    @Excel(name = "邮箱", orderNum = "4",width = 20)
    private String email;
    private Long deptId;
    @Excel(name = "部门", orderNum = "5")
    private String deptName;

}
