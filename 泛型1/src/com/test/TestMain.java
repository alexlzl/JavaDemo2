package com.test;

public class TestMain extends TestI<Object>{

	public TestMain(Object p) {
		super(p);
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		TestI<Object> t=new TestI<Object>(new TestMain("s"));
	}

}
