package com.test;

public enum 包含抽象方法的枚举类 {
	
	
	// 用于执行加法运算
	PLUS() { // 花括号部分其实是一个匿名内部子类

		@Override
		public double calculate(double x, double y) {
			return x + y;
		}

	},

	// 用于执行减法运算
	MINUS { // 花括号部分其实是一个匿名内部子类

		@Override
		public double calculate(double x, double y) {
			// TODO Auto-generated method stub
			return x - y;
		}

	},

	// 用于执行乘法运算
	TIMES { // 花括号部分其实是一个匿名内部子类

		@Override
		public double calculate(double x, double y) {
			return x * y;
		}

	},

	// 用于执行除法运算
	DIVIDE { // 花括号部分其实是一个匿名内部子类

		@Override
		public double calculate(double x, double y) {
			return x / y;
		}

	};
	private 包含抽象方法的枚举类(String s){};
	private 包含抽象方法的枚举类(){};

	// 为该枚举类定义一个抽象方法，枚举类中所有的枚举值都必须实现这个方法
	public abstract double calculate(double x, double y);
	
	public static void main(String[] str){
		System.out.print(包含抽象方法的枚举类.DIVIDE.calculate(10, 20)+"===");
	}
}
