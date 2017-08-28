package com.test;

public class Test implements ITest{

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		枚举单例 ts=	枚举单例.INSTANCE;
		ts.test();
		静态内部类.getInstance().test();
	}

}
