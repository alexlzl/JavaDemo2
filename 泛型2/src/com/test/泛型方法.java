package com.test;

public class 泛型方法 {
	class Demo {
		public <T> T fun(T t) { // 可以接收任意类型的数据
			return t; // 直接把参数返回
		}
		public <T> String get(T tt){
			return "";
		}
	};

	public static void main(String args[]) {
		Demo d = new 泛型方法().new Demo(); // 实例化Demo对象
		String str = d.fun("汤姆"); // 传递字符串
		int i = d.fun(30); // 传递数字，自动装箱
		System.out.println(str); // 输出内容
		System.out.println(i); // 输出内容
	}
}
