package com.zhang.jwt.web.interceptor;

import java.lang.annotation.*;

@Inherited
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SameUrlData {
    /**
     * 接口限制时间
     * @return
     */
     long value() default 1000;

}