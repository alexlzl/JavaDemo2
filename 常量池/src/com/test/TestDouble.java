package com.test;
/**
 * 两种浮点数类型的包装类Float,Double并没有实现常量池技术。
 * @author lzl
 *
 */

public class TestDouble {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Integer i7=400;
		Integer i8=400;
		Double d1=1.2;
		Double d2=1.2;
		System.out.println(d1==d2);//输出false
	}

}
