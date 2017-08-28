package com.ease.cred;

/**
 * 基本类型	  ==	equals
  字符串变量	  对象在内存中的首地址	字符串内容
  非字符串变量	对象在内存中的首地址	对象在内存中的首地址
  基本类型	  值	   不可用
  包装类	     地址	内容
 * @author lzl
 *
 */


public class Test {
	private final static Test t=new Test();
	
	private double ab = 12.22;
	private char[] cha = new char[9];
	private char[] char1 = new char[] { 'a', 'b', 'c' };
	// Java字符采用Unicode（全球语言统一编码）编码，每个字符占两个字节
	// Java中允许使用转义字符'\'将其后面的字符转换成其他含义， 如： char c = '\n'; //表示换行
	char c = 'w';
	char a = '中';

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Integer i1 = 100;
//		Integer i2=100;
		Integer i2 = new Integer("100");
		System.out.println(i2.intValue());
		Integer i3 = 200;
		Integer i4 = 200;
		System.out.println(i1 == i2);// true
		System.out.println(i3 == i4);// false
		Double i1d = 100.0;
		Double i2d = 100.0;
		Double i3d = 200.0;
		Double i4d = 200.0;

		System.out.println(i1d == i2d);// false
		System.out.println(i3d == i4d);// fasle;
	}

}
