package com.test;
/**
 * 设置标记位,检测中断标记来判断是否跳出线程
 * @author lzl
 *
 */

public class TestThread2 implements Runnable{ 
	 
    boolean stop = false; 
    public static void main(String[] args) throws Exception { 
        Thread thread = new Thread(new TestThread2(),"My Thread2"); 
        System.out.println( "Starting thread..." ); 
        thread.start(); 
        Thread.sleep( 3000 ); 
        System.out.println( "Interrupting thread..." ); 
        thread.interrupt(); 
        System.out.println("线程是否中断：" + thread.isInterrupted()); 
        Thread.sleep( 3000 ); 
        System.out.println("Stopping application..." ); 
    } 
    public void run() { 
        while(!stop){ 
            System.out.println( "My Thread is running..." ); 
            // 让该循环持续一段时间，使上面的话打印次数少点 
            long time = System.currentTimeMillis(); 
            while((System.currentTimeMillis()-time < 1000)) { 
            } 
            if(Thread.currentThread().isInterrupted()) { 
                return; 
            } 
        } 
        System.out.println("My Thread exiting under request..." ); 
    } 
} 