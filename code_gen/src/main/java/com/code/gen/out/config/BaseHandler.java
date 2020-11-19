package com.code.gen.out.config;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;

@Slf4j
@Component
public class BaseHandler implements MetaObjectHandler {

    // 新增的时候 填充
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("insert...");
        this.setFieldValByName("createTime", DateTime.now(), metaObject);
        this.setFieldValByName("updateTime", DateTime.now(), metaObject);
        // 如果没有设置值 就去填充,否则不进行填充
        Object obj = this.getFieldValByName("createTime", metaObject);
        if (obj != null) {
            this.setFieldValByName("createTime", DateTime.now(), metaObject);
        }
    }

    // 修改的时候，填充
    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("update...");
        this.setFieldValByName("updateTime", DateTime.now(), metaObject);
        // 如果没有设置值 就去填充,否则不进行填充
        Object obj = this.getFieldValByName("updateTime", metaObject);
        if (obj != null) {
            this.setFieldValByName("updateTime", DateTime.now(), metaObject);
        }
    }


}