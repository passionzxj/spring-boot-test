package com.code.gen.out.common;

import lombok.Data;

import java.io.Serializable;

@Data
public class BaseQuery implements Serializable {
    private Integer pageNum;
    private Integer pageSize;

    {
        this.pageNum = 1;
        this.pageSize = 10;
    }
}
