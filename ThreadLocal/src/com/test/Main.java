package com.test;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		 ThreadLocalThread a = new ThreadLocalThread("ThreadA");
		
	        ThreadLocalThread b = new ThreadLocalThread("ThreadB");
	        ThreadLocalThread c = new ThreadLocalThread("ThreadC");
	        a.start();
	        try {
				a.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        b.start();
	        try {
				b.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        c.start();
	}

}
