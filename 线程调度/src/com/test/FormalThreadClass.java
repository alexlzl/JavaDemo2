package com.test;

public class FormalThreadClass {
	 public static void main(String[] args) {
		 MyTask1 myTask1 = new MyTask1();
		 MyTask1 myTask2 = new MyTask1();
         Thread thread1 = new Thread( new MyRunnable(myTask1));
         Thread thread2 = new Thread( new MyRunnable(myTask2));
         thread1.start();
         thread2.start();
  }
}
