package com.zhang.jwt.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("t_dictionary")
public class Dictionary {

    private Long id;
    private String name;
    private String sn;
    private String intro;
}
