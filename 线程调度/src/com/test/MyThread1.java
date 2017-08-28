package com.test;

public class MyThread1 extends Thread {
	 @Override
     public void run() {
            for( int i=0;i<10;i++)
                  System. out.println( "thread 1 counter:"+i);
    }      
}
