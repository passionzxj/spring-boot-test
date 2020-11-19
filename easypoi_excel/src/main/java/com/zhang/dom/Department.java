package com.zhang.dom;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("t_department")
public class Department {

    private Long id;
    private String name;
    private String Address;
}
