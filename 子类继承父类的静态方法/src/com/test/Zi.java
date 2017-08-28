package com.test;
/**
 * 父类的静态方法可以被子类继承，但是不能重写
 * @author lzl
 *
 */
public class Zi extends Fu {
	public static void main(String[] args) {
		Fu fu = new Zi();
		/**
		 * 父类的静态方法 子类的一般方法
		 */
		fu.show();
		fu.method();
		show();
	}

	public static void show() {
		System.out.println("子类的静态");
	}

	public void method() {
		System.out.println("子类的一般方法");
	}
}
