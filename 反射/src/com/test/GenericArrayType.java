package com.test;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.List;

public class GenericArrayType<T> {
	public static void main(String[] args) throws Exception {
		Method method = Test.class.getDeclaredMethods()[0];
		// public void
		// com.test.Test.show(java.util.List[],java.lang.Object[],java.util.List,java.lang.String[],int[])
		System.out.println(method);
		Type[] types = method.getGenericParameterTypes(); // 这是 Method 中的方法
		for (Type type : types) {
			System.out.println(type instanceof GenericArrayType);
		}
	}

	static class Test<T> {
		public void show(List<String>[] pTypeArray, T[] vTypeArray, List<String> list, String[] strings, int[] ints) {
		}
	}
}
