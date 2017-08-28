package com.test;

import java.util.concurrent.atomic.AtomicInteger;

public class ThreadLocalThread extends Thread
{
    private static AtomicInteger ai = new AtomicInteger();
    
    public ThreadLocalThread(String name)
    {
        super(name);
    }
    
    public void run()
    {
        try
        {
            for (int i = 0; i < 3; i++)
            {
                Tools.t1.set(ai.addAndGet(1) + "");
                System.out.println(this.getName() + " get value--->" + Tools.t1.get());
                Thread.sleep(200);
            }
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }
}