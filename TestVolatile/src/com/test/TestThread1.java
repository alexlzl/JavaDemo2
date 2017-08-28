package com.test;

public class TestThread1 {
 public static void  main(String[] str){
		Thread t1 = new Thread(new Runnable() {

			@Override
			public void run() {
				System.out.println("t1");
			}
		});
//		try {
//			//引用t1线程，等待t1线程执行完
//			t1.join();
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
		Thread t2 = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					//引用t1线程，等待t1线程执行完
					t1.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println("t2");
			}
		});
//		try {
//			//引用t2线程，等待t2线程执行完
//			t2.join();
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
		Thread t3 = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					//引用t2线程，等待t2线程执行完
					t2.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println("t3");
			}
		});
		
		t1.start();
		t2.start();
		t3.start();
		
		
  }
}
