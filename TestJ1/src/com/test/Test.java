package com.test;

public class Test {
	private static String str="未处理&lt;font color=#f78201> %1$s &lt;/font>项";

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println(String.format(str, (50 < 100 ? String.valueOf(50) : "99+")));
		//未处理&lt;font color=#f78201> 50 &lt;/font>项


	}

}
