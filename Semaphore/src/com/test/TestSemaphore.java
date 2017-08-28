package com.test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

public class TestSemaphore {

    public static void main(String[] args) {
    	short s1=1;

    	s1 = (short) (s1+1);  
    	String str="abc";
    	int   is   =   1;  
    	String sub=str+is;
    	char   cc= 'c'; 
    	cc   +=is;        //编译通过 
    	 System.out.println((int)cc);
    	byte ba = 1; 
    	byte bb = 1; 
    	byte c = (byte)(ba + bb); 
    	 System.out.println(c);

    char aa = '2';
    int i = Integer.parseInt(String.valueOf(aa));
    System.out.println(i);
    String ab="test";
    Integer a=10;
    Integer b=10;
    System.out.println(a.toString());
    System.out.println(a==b);
    System.out.println(2.0-1.9);
    System.out.println("H" + "a");
    System.out.println('H' + 'a');
    try {   
    	  System.out.println("try");
    } finally {   
    	  System.out.println("finally");
    }   
   switch(ab){
   case "a":
	   System.out.println("a");
	   break;
   case "test":
	   System.out.println("test");
	   break;
   }
    // 线程池

    ExecutorService exec = Executors.newCachedThreadPool();

    // 只能5个线程同时访问

    final Semaphore semp = new Semaphore(5);

     // 模拟20个客户端访问

     for (int index = 0; index < 20; index++) {

                  final int NO = index;

                  Runnable run = new Runnable() {

                                     public void run() {

                                                try {

                                                        // 获取许可

                                                        semp.acquire();

                                                        System.out.println("Accessing: " + NO);

                                                        Thread.sleep((long) (Math.random() * 10000));

                                                        // 访问完后，释放

                                                        semp.release();

                                                        System.out.println("-----------------"+semp.availablePermits());

                                                } catch (InterruptedException e) {

                                                        e.printStackTrace();

                                                }

                                      }

                          };

          exec.execute(run);

 }

 // 退出线程池

 exec.shutdown();

}

} 
