package com.code.gen.out.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD,ElementType.PARAMETER})
@Documented
public @interface UserOperate {
 
	//模块名  
    String moduleName() default "";
  	//操作名  
    String optionName() default "";
    //操作内容  
    String option() default ""; 
    //用户
    String userId() default ""; 
}