package com.test;

public class Parent <Test> extends Parent1{
  public Parent(){
	  System.out.println("p==="+getClass()+"==");
  }
  
  public void testp(){
	  System.out.println("testp==="+getClass());
  }
}
