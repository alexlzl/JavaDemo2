package com.test;

public class TestThread {
	static int i=0;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
	
		
		for( i=0;i<5;i++){
			
			new Thread(new Runnable(){

				@Override
				public void run() {
					// TODO Auto-generated method stub
					System.out.println("执行线程"+Thread.currentThread().getId());
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					System.out.println("线程"+Thread.currentThread().getId()+"over");
				}
				
			}).start();
			System.out.println("i=="+i);
		}
//		new Thread(new Runnable(){
//
//			@Override
//			public void run() {
//				// TODO Auto-generated method stub
//				System.out.println("线程1");
//				try {
//					Thread.sleep(2000);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				System.out.println("线程1over");
//			}
//			
//		}).start();
//		new Thread(new Runnable(){
//
//			@Override
//			public void run() {
//				// TODO Auto-generated method stub
//				System.out.println("线程2");
//				try {
//					Thread.sleep(3000);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				System.out.println("线程2over");
//			}
//			
//		}).start();

	}

}
