package com.test;

/**
 * 然后我们使用javac编译上面的类,得到class文件.
 * 
 * 1 javac Season.java
 * 然后,我们利用反编译的方法来看看字节码文件究竟是什么.这里使用的工具是javap的简单命令,先列举一下这个Season下的全部元素.
 * 
 * 1 2 3 4 5 6 7 8 9 10 11 12 ➜ company javap Season Warning: Binary file Season
 * contains com.company.Season Compiled from "Season.java" public final class
 * com.company.Season extends java.lang.Enum<com.company.Season> { public static
 * final com.company.Season SPRING; public static final com.company.Season
 * SUMMER; public static final com.company.Season AUTUMN; public static final
 * com.company.Season WINTER; public static com.company.Season[] values();
 * public static com.company.Season valueOf(java.lang.String); static {}; }
 * 从上反编译结果可知
 * 
 * java代码中的Season转换成了继承自的java.lang.enum的类
 * 既然隐式继承自java.lang.enum,也就意味java代码中,Season不能再继承其他的类 Season被标记成了final,意味着它不能被继承
 * 
 * @author lzl
 *
 */
public enum 枚举原理 {
	SPRING, SUMMER, AUTUMN, WINTER
}
