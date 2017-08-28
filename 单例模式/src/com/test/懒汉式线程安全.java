package com.test;

public class 懒汉式线程安全 {
	private static 懒汉式线程安全 instance;

	private 懒汉式线程安全() {
	}

	public synchronized static 懒汉式线程安全 getInstance() {
		if (instance == null) {
			instance = new 懒汉式线程安全();
		}
		return instance;
	}
}
