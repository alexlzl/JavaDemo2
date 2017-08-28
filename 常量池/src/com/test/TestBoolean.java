package com.test;

public class TestBoolean {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		boolean b0=true;
		Boolean b1=true;
		Boolean b2=true;
		Boolean b3=new Boolean(true);
		Boolean b4=new Boolean(true);
		System.out.println(b1==b2);//true
		System.out.println(b3==b4);//false
		System.out.println(b0==b1);//true

	}

}
