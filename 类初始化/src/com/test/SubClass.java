package com.test;

/**
 * 
 * @author lzl
 *         我们大家都知道，对于静态变量、静态初始化块、变量、初始化块、构造器，它们的初始化顺序依次是（静态变量、静态初始化块）>（变量、初始化块）>
 *         构造器。我们也可以通过下面的测试代码来验证这一点
 */
public class SubClass extends Parent {
	// 静态变量
	public static String s_StaticField = "子类--静态变量";
	// 变量
	public String s_Field = "子类--变量";
	// 静态初始化块
	static {
		System.out.println(s_StaticField);
		System.out.println("子类--静态初始化块");
	}
	// 初始化块
	{
		System.out.println(s_Field);
		System.out.println("子类--初始化块");
	}

	// 构造器
	public SubClass() {
		System.out.println("子类--构造器");
	}

	// 程序入口
	public static void main(String[] args) {
		new SubClass();
	}
}
