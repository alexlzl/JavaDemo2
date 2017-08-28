package com.test;

public class ThreadFlag extends Thread {
	public volatile boolean exit = false;

	public void run() {
		while (!exit){
			 try {
					
					while(true){
						sleep(1000);
						System.out.println("线程执行!");
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
			
			
		}
		
		
			
			
	}

	public static void main(String[] args) throws Exception {
		ThreadFlag thread = new ThreadFlag();
		thread.start();
		sleep(1000); // 主线程延迟5秒
		thread.exit = true; // 终止线程thread
		System.out.println("线程退出!");
	}
}