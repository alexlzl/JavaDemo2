package com.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 但是由于类型擦除机制，类型参数并不能用来创建对象或是作为静态变量的类型。考虑下面的泛型类中的正确和错误的用法。
 * 
 * @author lzl
 *
 * @param <X>
 * @param <Y>
 * @param <Z>
 */
public class 类型擦除机制<X extends Number, Y, Z> {
	List<String>[] l;
	private X x;
	// private static Y y; // 编译错误，不能用在静态变量中

	public X getFirst() {
		// 正确用法
		return x;
	}

	public void wrong() {
		// Z z = new Z(); // 编译错误，不能创建对象
	}

	/**
	 * 显然在平时使用中，ArrayList<Integer>()和new ArrayList
	 * <String>()是完全不同的类型，但是在这里，程序却的的确确会输出true。
	 * 
	 * 这就是Java泛型的类型擦除造成的，因为不管是ArrayList<Integer>()还是new ArrayList
	 * <String>()，都在编译器被编译器擦除成了ArrayList。那编译器为什么要做这件事？原因也和大多数的Java让人不爽的点一样——兼容性。
	 * 由于泛型并不是从Java诞生就存在的一个特性，而是等到SE5才被加入的，所以为了兼容之前并未使用泛型的类库和代码，
	 * 不得不让编译器擦除掉代码中有关于泛型类型信息的部分，这样最后生成出来的代码其实是『泛型无关』的，
	 * 我们使用别人的代码或者类库时也就不需要关心对方代码是否已经『泛化』，反之亦然。
	 * 
	 * 在编译器层面做的这件事（擦除具体的类型信息），使得Java的泛型先天都存在一个让人非常难受的缺点：
	 * 
	 * 在泛型代码内部，无法获得任何有关泛型参数类型的信息
	 * 
	 * @param str
	 */
	public static void main(String[] str) {

		Class c1 = new ArrayList<Integer>().getClass();
		Class c2 = new ArrayList<String>().getClass();
		System.out.println(c1 == c2);
		
		List<Integer> list = new ArrayList<Integer>();
		Map<Integer, String> map = new HashMap<Integer, String>();
		System.out.println(Arrays.toString(list.getClass().getTypeParameters()));
		System.out.println(Arrays.toString(map.getClass().getTypeParameters()));

	}
}
