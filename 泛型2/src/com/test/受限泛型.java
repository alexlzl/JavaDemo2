package com.test;

public class 受限泛型 {
	class Info<T> {
		private T var; // 定义泛型变量

		public void setVar(T var) {
			this.var = var;
		}

		public T getVar() {
			return this.var;
		}

		public String toString() { // 直接打印
			return this.var.toString();
		}
	};

	public static void main(String args[]) {
		Info<Integer> i1 = new 受限泛型().new Info<Integer>(); // 声明Integer的泛型对象
		Info<Float> i2 = new 受限泛型().new Info<Float>(); // 声明Float的泛型对象
		i1.setVar(30); // 设置整数，自动装箱
		i2.setVar(30.1f); // 设置小数，自动装箱
		fun(i1);
		fun(i2);
		Info<String> i3 = new 受限泛型().new Info<String>(); // 声明String的泛型对象
		Info<Object> i4 = new 受限泛型().new Info<Object>(); // 声明Object的泛型对象
		i3.setVar("hello");
		i4.setVar(new Object());
		fun1(i3);
		fun1(i4);
	}

	public static void fun(Info<? extends Number> temp) { // 只能接收Number及其Number的子类
		System.out.println(temp + "、");
	}

	public static void fun1(Info<? super String> temp) { // 只能接收String或Object类型的泛型
		System.out.println(temp + "、");
	}
}
