package com.test;

import java.util.ArrayList;
import java.util.Collection;

public class GenMethod {
	/**
	 * 方法添加范型
	 * @param a
	 * @param c
	 */
	public static  <T> void fromArrayToCollection(T[] a,Collection<T> c){
		for (T t : a) {
		  c.add(t);
		}
	}
	
	public static void main(String[] args) {
		Object[] oa = new Object[100];
		Collection<Object> co = new ArrayList<>();
		
		GenMethod.<Object>fromArrayToCollection(oa, co);
	}		
}
