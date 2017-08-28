package com.test;

/**
 * 创建枚举默认就是线程安全的，所以不需要担心double checked
 * locking，而且还能防止反序列化导致重新创建新的对象。但是还是很少看到有人这样写，可能是因为不太熟悉吧。
 * 
 * @author lzl
 *
 */
public enum 枚举单例 {
	INSTANCE;
	public void test() {
		System.out.println("test枚举单例");
	};
}
