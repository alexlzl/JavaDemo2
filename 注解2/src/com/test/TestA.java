package com.test;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * 四个元注解分别是：@Target,@Retention,@Documented,@Inherited ，再次强调下元注解是java
 * API提供，是专门用来定义注解的注解，其作用分别如下：
 * 
 * @Target 表示该注解用于什么地方，可能的值在枚举类 ElemenetType 中，包括：
 *         ElemenetType.CONSTRUCTOR----------------------------构造器声明
 *         ElemenetType.FIELD --------------------------------------域声明（包括 enum
 *         实例） ElemenetType.LOCAL_VARIABLE------------------------- 局部变量声明
 *         ElemenetType.METHOD ----------------------------------方法声明
 *         ElemenetType.PACKAGE --------------------------------- 包声明
 *         ElemenetType.PARAMETER ------------------------------参数声明
 *         ElemenetType.TYPE---------------------------------------
 *         类，接口（包括注解类型）或enum声明
 * 
 * @Retention 表示在什么级别保存该注解信息。可选的参数值在枚举类型 RetentionPolicy 中，包括：
 *            RetentionPolicy.SOURCE ---------------------------------注解将被编译器丢弃
 *            RetentionPolicy.CLASS
 *            -----------------------------------注解在class文件中可用，但会被VM丢弃
 *            RetentionPolicy.RUNTIME VM-------将在运行期也保留注释，因此可以通过反射机制读取注解的信息。
 * 
 * @Documented 将此注解包含在 javadoc 中
 *             ，它代表着此注解会被javadoc工具提取成文档。在doc文档中的内容会因为此注解的信息内容不同而不同。相当与@see,
 *             @param 等。
 * 
 * @Inherited 允许子类继承父类中的注解。
 * @author lzl
 *
 */
public @interface TestA {

}
