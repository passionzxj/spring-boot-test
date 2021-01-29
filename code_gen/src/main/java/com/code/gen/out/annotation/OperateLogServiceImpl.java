package com.code.gen.out.annotation;

import org.springframework.stereotype.Service;

@Service
public class OperateLogServiceImpl implements OperationLogService {
    @Override
    public void insert(OperationLog operationLog) {

        System.out.println("入库操作。。。。。。。。。。。。");
    }
}
