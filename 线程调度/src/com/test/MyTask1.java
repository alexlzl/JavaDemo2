package com.test;

public class MyTask1 {
	   public static synchronized void doTask() {
           for ( int i = 0; i < 5; i++) {
                 System. out.println(Thread. currentThread().getName()+" running "+i);
          }
   }
}
