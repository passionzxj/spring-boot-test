package com.zhang.jwt.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    // 新增的时候 填充
    @Override
    public void insertFill(MetaObject metaObject) {
        if (metaObject.hasSetter("createTime")) {
            System.out.println("insert....");
            this.setInsertFieldValByName("createTime", LocalDateTime.now(), metaObject);
        }
        // 如果没有设置值 就去填充,否则不进行填充
        Object obj = this.getFieldValByName("createTime", metaObject);
        if (obj != null) {
            this.setUpdateFieldValByName("createTime", LocalDateTime.now(), metaObject);
        }
    }

    // 修改的时候，填充
    @Override
    public void updateFill(MetaObject metaObject) {
        if (metaObject.hasSetter("updateTime")) {
            System.out.println("update...");
            this.setUpdateFieldValByName("updateTime", LocalDateTime.now(), metaObject);
        }
        // 如果没有设置值 就去填充,否则不进行填充
        Object obj = this.getFieldValByName("updateTime", metaObject);
        if (obj != null) {
            this.setUpdateFieldValByName("updateTime", LocalDateTime.now(), metaObject);
        }
    }



}