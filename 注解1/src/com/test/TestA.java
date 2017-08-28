package com.test;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/* 
 * 定义注解 Test 
 * 为方便测试：注解目标为类 方法，属性及构造方法 
 * 注解中含有三个元素 id ,name和 gid; 
 * id 元素 有默认值 0
 */ 
@Target({ElementType.METHOD, ElementType.TYPE,ElementType.FIELD ,ElementType.CONSTRUCTOR})
@Retention(RetentionPolicy.RUNTIME)
public @interface TestA {
	String name();
	int id() default 0;
	Class<Long> gid();
}
