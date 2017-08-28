package com.test;

public class 线程调度 {
	 public static void main(String[] args) {
         Thread th1= new MyThread1();
         Thread th2= new MyThread2();
         th1.start();
         th2.start();
  }
	 
	
}
