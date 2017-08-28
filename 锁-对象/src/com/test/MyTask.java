package com.test;
/**
 * 1 is running
1 is running
1 is running
1 is running
1 is running
2 is running
2 is running
2 is running
2 is running
2 is running

 * @author lzl
 *
 */

public class MyTask {
	public void doTask1() {
//		for (int i = 0; i < 10; i++) {
//			System.out.println("1 is running");
//		}
		synchronized (this) {
			for (int i = 0; i < 5; i++) {
				System.out.println("1 is running");
			}
		}

	}

	public void doTask2() {
		for (int i = 0; i < 10; i++) {
			System.out.println("2 is running");
		}
	}
}
