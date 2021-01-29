package com.zhang.jwt.domain.dto;

import lombok.Data;

import java.util.Date;

@Data
public class UserDto {
    private String id;
    private String name;
    private Integer age;
    private String email;
    private String sex;
    private Date birthday;
    private Long deptId;
}
