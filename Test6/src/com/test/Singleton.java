package com.test;

public class Singleton {
	private volatile static Singleton instance;

	private Singleton() {
	}

	public static Singleton getSingleton() {
		if (instance == null) { // Single Checked
			synchronized (Singleton.class) {
				if (instance == null) { // Double Checked
					instance = new Singleton();
				}
			}
		}
		return instance;
	}

}
