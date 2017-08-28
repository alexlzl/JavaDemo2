package com.test;

/**
 * 这种写法仍然使用JVM本身机制保证了线程安全问题；由于 SingletonHolder 是私有的，除了 getInstance()
 * 之外没有办法访问它，因此它是懒汉式的；同时读取实例的时候不会进行同步，没有性能缺陷；也不依赖 JDK 版本。
 * 
 * @author lzl
 *
 */
public class 静态内部类 {
	private String 今天;

	private static class SingletonHolder {
		private static final 静态内部类 INSTANCE = new 静态内部类();
	}

	private 静态内部类() {
	}

	public static final 静态内部类 getInstance() {
		return SingletonHolder.INSTANCE;
	}

	public void test() {
		System.out.println("静态内部类===test");
	};

	public void 金() {
	};
}
