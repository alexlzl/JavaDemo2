package com.test;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;

public class Test {
	private static float num = 1.2f;
	private static Foo[] str = new Foo[0];

	public static void main(String[] args) {
		// TODO Auto-generated method stub
	  str=	minmax(str);
	  System.out.println("str==="+Arrays.toString(str));
		Foo<String> foo = new Foo<String>();
		// 浮点型被0除，得到无穷大值
		System.out.println((num / 0) + "");
		// 整型值被0除，抛异常
		// System.out.println((10/0)+"");
		int m = 10;
		int n = 10;
		int a = 2;
		int b = 2;
		System.out.println((a * (++m)) + "");
		System.out.println((b * (n++)) + "");
		int i = 10;
		short j = 2;
		long k = 21l;
		byte l = 11;
		float f = 10.2f;
		m = (int) f;
		System.out.println(m + "===");
		i = j;
		k = l;
		f = i;
		System.out.println(i + "===" + k + "==" + f);
		System.out.println(getInt(2) + "==================");
		ArrayList<Integer> arrayList3 = new ArrayList<Integer>();
		arrayList3.add(1);// 这样调用add方法只能存储整形，因为泛型类型的实例为Integer
		try {
			arrayList3.getClass().getMethod("add", Object.class).invoke(arrayList3, "asd");
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (int ii = 0; ii < arrayList3.size(); ii++) {
			System.out.println(arrayList3.get(ii));
		}

	}

	// 这是一个简单的泛型方法
	public static <T extends Foo> int add(T y) {
		return y.getInt();
	}
	// public<T> T[] minMax(T[] a){
	// T[] mm = new T[2]; //ERROR
	// }
  /**
   * 利用反射构造泛型数组
   * @param a
   * @return
   */
	public static <T > T[] minmax(T[] a)

	{

		T[] mm = (T[]) Array.newInstance(a.getClass().getComponentType(), 2);
		return mm;

		// 以替换掉以下代码

		// Obeject[] mm = new Object[2];

		// return (T[]) mm;

	}
	/**
	 * 泛型方法的定义和使用
	 * @param t
	 * @param u
	 * @return
	 */
	public static <T, U> T get(T t, U u) {  
        if (u != null)  
            return t;  
        else  
            return null;  
    }  
	private static int getInt(int n) {

		try {
			int r = n * n;
			return r;
		} finally {
			if (n == 2) {
				return n;
			}
		}

	}

}
