package com.test;

/**
 * 如使在静态方法中用synchronized时，因为这个方法就不是仅属于某个对象而是属于整个类的了，
 * 所以一旦一个线程进入了这个代码块就会将这个类的所有对象的所有synchronized方法或synchronized同步代码块锁定，
 * 其他的线程就没有办法访问所有这些对象的synchronized方法和synchronized代码块（
 * 注意其他线程还是仍然能访问这些对象的非synchronized方法和synchronized代码块的），因此这种锁定是class级别的。
 * 
 * 
 * 
 * 如在使用synchronized(.class)时，一旦一个线程进入了这个代码块就会将整个类的所有这个synchronized(.class)
 * 同步代码块锁定，其他的线程就没有办法访问这个对象的synchronized(**.class)
 * 代码块，这种锁也是class级别的，但要注意在这种情况下，其他线程仍然是可以访问仅做了synchronized的代码块或非静态方法的，
 * 因为它们仅仅是对当前对象的锁定。
 * 
 * @author lzl
 *
 */
public class 锁类 {
	public void test1() {
		synchronized (锁类.class) {
			int i = 5;
			while (i-- > 0) {
				System.out.println(Thread.currentThread().getName() + " : " + i);
				try {
					Thread.sleep(500);
				} catch (InterruptedException ie) {
				}
			}
		}
	}

	public static synchronized void test2() {
		int i = 5;
		while (i-- > 0) {
			System.out.println(Thread.currentThread().getName() + " : " + i);
			try {
				Thread.sleep(500);
			} catch (InterruptedException ie) {
			}
		}
	}

	public static void main(String[] args) {
		final 锁类 myt2 = new 锁类();
		Thread test1 = new Thread(new Runnable() {
			public void run() {
				myt2.test1();
			}
		}, "test1");
		Thread test2 = new Thread(new Runnable() {
			public void run() {
				锁类.test2();
			}
		}, "test2");
		test1.start();
		test2.start();
		// TestRunnable tr=new TestRunnable();
		// Thread test3=new Thread(tr);
		// test3.start();
	}
}
