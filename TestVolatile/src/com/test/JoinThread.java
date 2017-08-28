package com.test;

/**
 * 
 * @author lzl
 *         volatile关键字相信了解Java多线程的读者都很清楚它的作用。volatile关键字用于声明简单类型变量，如int、float、
 *         boolean等数据类型。如果这些简单数据类型声明为volatile，对它们的操作就会变成原子级别的。但这有一定的限制
 * 
 *         原因是声明为volatile的简单变量如果当前值由该变量以前的值相关，那么volatile关键字不起作用，
 *         也就是说如下的表达式都不是原子操作：
 * 
 *         n = n + 1 ; n ++ ;
 * 
 */

public class JoinThread extends Thread {
	public static volatile int n = 0;

	public static synchronized void inc() {
		n++;
	}

	public void run() {
		for (int i = 0; i < 10; i++)
			try {
				inc(); // n = n + 1 改成了 inc();
				sleep(3); // 为了使运行结果更随机，延迟3毫秒

			} catch (Exception e) {
			}
	}

	public static void main(String[] args) throws Exception {

		Thread threads[] = new Thread[100];
		for (int i = 0; i < threads.length; i++)
			// 建立100个线程
			threads[i] = new JoinThread();
		for (int i = 0; i < threads.length; i++)
			// 运行刚才建立的100个线程
			threads[i].start();
		for (int i = 0; i < threads.length; i++)
			// 100个线程都执行完后继续
			threads[i].join();
		System.out.println(" n= " + JoinThread.n);
	}
}
