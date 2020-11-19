package com.zhang.domain;


import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@TableName("t_user")
public class User {
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;
    private String name;
    private Integer age;
    private String email;
    private String sex;
    private Date birthday;
    private Long deptId;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;

    public void setSex(String sex) {
        this.sex = sex.equals(1+"") ? "男" : "女";
    }
}
