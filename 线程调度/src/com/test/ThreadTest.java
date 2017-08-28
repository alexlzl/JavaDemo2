package com.test;

public class ThreadTest {
	public static void main(String[] args) {
		final MyTask myTask = new MyTask();
		Thread thread1 = new Thread(new Runnable() {
			public void run() {
				myTask.doTask1();
			}
		});
		Thread thread2 = new Thread(new Runnable() {
			public void run() {
				myTask.doTask2();
			}
		});
		thread1.start();
		thread2.start();
	}
}
