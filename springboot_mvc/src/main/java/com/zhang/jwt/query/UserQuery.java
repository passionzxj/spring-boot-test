package com.zhang.jwt.query;

import lombok.Data;

@Data
public class UserQuery {

    private Long pageNum = 1L;
    private Long PageSize = 10L;
    private String name;
    private  Integer minAge = 0;
    private  Integer maxAge = 150;

}
