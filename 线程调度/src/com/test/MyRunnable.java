package com.test;

class MyRunnable implements Runnable {
	MyTask1 myTask;
     public MyRunnable(MyTask1 myTask) {
            this. myTask = myTask;
    }
     @Override
     public void run() {
    	 MyTask1. doTask();
    }
}