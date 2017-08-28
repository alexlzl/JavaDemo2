package com.test;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class TestMultiThread  implements Runnable{

	private static int i;
	
	private static volatile Integer vi = 0;
	
	private static AtomicInteger ai = new AtomicInteger();
	
	private static volatile Integer si = 0;
	
	private static int ri;
	
	private static AtomicInteger flag = new AtomicInteger();
	
	private Lock lock = new ReentrantLock();
	
	@Override
	public void run() {
		synchronized(TestMultiThread.class){
			System.out.println("Thread===="+Thread.currentThread().getId());
		
			for(int k=0;k<200000;k++){
				i++;
				vi++;
				ai.incrementAndGet();
				si=si+1;
				ri=ri+1;
//				synchronized(this){
//					si=si+1;
//				}
//				lock.lock();
//				try{
//					ri=ri+1;
//				}finally{
//					lock.unlock();
//				}
				
			
		}
			System.out.println("Thread===="+Thread.currentThread().getId()+"遍历完成");
		}
		
		flag.incrementAndGet();
	}
	
	public static void main(String[] args) throws InterruptedException{
		TestMultiThread t1 = new TestMultiThread();
		TestMultiThread t2 = new TestMultiThread();
		ExecutorService exec1 = Executors.newCachedThreadPool();
		ExecutorService exec2 = Executors.newCachedThreadPool();
		exec1.execute(t1);
		exec2.execute(t2);
		
		
		while(true){
			if(flag.intValue()==2){
				System.out.println("i>>>>>"+i);
				System.out.println("vi>>>>>"+vi);
				System.out.println("ai>>>>>"+ai);
				System.out.println("si>>>>>"+si);	
				System.out.println("ri>>>>>"+ri);	
				break;
			}
			Thread.sleep(50);
		}
//		while(true){
//			if(flag.intValue()==1){
//				System.out.println("i>>>>>"+i);
//				System.out.println("vi>>>>>"+vi);
//				System.out.println("ai>>>>>"+ai);
//				System.out.println("si>>>>>"+si);	
//				System.out.println("ri>>>>>"+ri);	
//				break;
//			}
//			Thread.sleep(50);
//			
//		}
		
	}
	
}
