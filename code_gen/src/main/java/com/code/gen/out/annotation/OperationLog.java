package com.code.gen.out.annotation;

import lombok.Data;

import java.util.Date;

@Data
public class OperationLog {
    private String id;
    private String operModule;
    private String operType;
    private String operDesc;
    private String operMethod;
    private String operUri;
    private String operIp;
    private String operUserId;
    private String operUserName;
    private Date operCreateTime;

}
