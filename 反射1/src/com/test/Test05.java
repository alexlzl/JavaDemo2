package com.test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

public class Test05 {
	public static void main(String[] args) {
		Class<?> demo = null;
		try {
			demo = Class.forName("com.test.Person01");
			try {
				Person01 p=(Person01) demo.newInstance();
				System.out.println(p.toString());
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		Constructor<?>[] cons = demo.getConstructors();// 返回一个包含某些 Constructor
														// 对象的数组，这些对象反映此 Class
														// 对象所表示的类的所有public构造方法。
		for (Constructor c : cons) {
			c.setAccessible(true);
			System.out.println("构造方法:" + c);
		}
		for (int i = 0; i < cons.length; i++) {
			Class types[] = cons[i].getParameterTypes();// 按照声明顺序返回一组 Class
														// 对象，这些对象表示此
														// Constructor
														// 对象所表示构造方法的形参类型。
			int mo = cons[i].getModifiers();// 以整数形式返回此 Constructor 对象所表示构造方法的
											// Java 语言修饰符。
			System.out.println("修饰==="+Modifier.toString(mo) + " ");
			System.out.println("构造方法名称==="+cons[i].getName());// 以字符串形式返回此构造方法的名称。
			for (int j = 0; j < types.length; j++) {
				System.out.println("构造方法参数类型==="+types[j].getName() );// 以 String
																// 的形式返回此 Class
																// 对象所表示的实体（类、接口、数组类、基本类型或
																// void）名称。
			
			}
		}
	}

}
