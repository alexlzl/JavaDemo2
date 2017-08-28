package com.test;

/**
 * 因为泛型类中的泛型参数的实例化是在定义对象的时候指定的，而静态变量和静态方法不需要使用对象来调用。对象都没有创建，如何确定这个泛型参数是何种类型，
 * 所以当然是错误的。 但是要注意区分下面的一种情况：
 * 
 * @author lzl
 *
 * @param <T>
 */

public class 泛型在静态方法和静态类中的问题<T> {
	// public static T one; //编译错误
	// public static T show(T one){ //编译错误
	// return null;
	// }
	public static <T> T show(T one) {// 这是正确的
		return null;
	}
	
	public static void main(String[] str){
		泛型在静态方法和静态类中的问题<Object> s=new 泛型在静态方法和静态类中的问题<Object>();
	}
}
