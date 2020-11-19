package com.zhang.domain.vo;

import com.zhang.domain.Department;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserVO {

    private String id;
    private String name;
    private Integer age;
    private String email;
    private Department department;
    private LocalDateTime createTime;
}
