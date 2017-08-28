package com.test;

public class MyTask {
	public synchronized void doTask1() {
		for (int i = 0; i < 5; i++) {
			System.out.println("1 This is real Tasking " + i);
		}
	}

	public void doTask2() {
		for (int i = 0; i < 5; i++) {
			System.out.println("2 This is real Tasking " + i);
		}
	}
}
