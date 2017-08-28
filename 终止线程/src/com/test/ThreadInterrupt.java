package com.test;

public class ThreadInterrupt extends Thread 
{ 
	   public void run() 
	    { 
		   
	        try 
	        { 
	        	
	            sleep(50000);  // 延迟50秒 
	        } 
	        catch (InterruptedException e) 
	        { 
	            System.out.println(e.getMessage()); 
	        } 
	        
	    	while(true){
	    		 try {
					sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
				System.out.println("线程执行!");
			}
	    } 
	    public static void main(String[] args) throws Exception 
	    { 
	        Thread thread = new ThreadInterrupt(); 
	        thread.start(); 
	        thread.interrupt(); 
	        System.out.println("线程已经退出!"); 
	       
	    } 
} 