package com.test;

import java.util.ArrayList;

/**
 * 在调用泛型方法的时候，可以指定泛型，也可以不指定泛型。 在不指定泛型的情况下，泛型变量的类型为 该方法中的几种类型的同一个父类的最小级，直到Object。
 * 在指定泛型的时候，该方法中的几种类型必须是该泛型实例类型或者其子类。
 * 
 * 
 * 其实在泛型类中，不指定泛型的时候，也差不多，只不过这个时候的泛型类型为Object，就比如ArrayList中，如果不指定泛型，
 * 那么这个ArrayList中可以放任意类型的对象。
 * 
 * @author lzl
 *
 */
public class 原始类型和泛型变量的类型 {
	public static void main(String[] args) {
		/** 不指定泛型的时候 */
		int i = 原始类型和泛型变量的类型.add(1, 2); // 这两个参数都是Integer，所以T为Integer类型
		Number f = 原始类型和泛型变量的类型.add(1, 1.2);// 这两个参数一个是Integer，以风格是Float，所以取同一父类的最小级，为Number
		Object o = 原始类型和泛型变量的类型.add(1, "asd");// 这两个参数一个是Integer，以风格是Float，所以取同一父类的最小级，为Object

		/** 指定泛型的时候 */
		int a = 原始类型和泛型变量的类型.<Integer> add(1, 2);// 指定了Integer，所以只能为Integer类型或者其子类
		// int b=原始类型和泛型变量的类型.<Integer>add(1, 2.2);//编译错误，指定了Integer，不能为Float
		Number c = 原始类型和泛型变量的类型.<Number> add(1, 2.2); // 指定为Number，所以可以为Integer和Float

		/**
		 * 这样是没有错误的，不过会有个编译时警告。 不过在第一种情况，可以实现与 完全使用泛型参数一样的效果，第二种则完全没效果。
		 * 因为，本来类型检查就是编译时完成的。new
		 * ArrayList()只是在内存中开辟一个存储空间，可以存储任何的类型对象。而真正涉及类型检查的是它的引用，
		 * 因为我们是使用它引用arrayList1 来调用它的方法，比如说调用add()方法。所以arrayList1引用能完成泛型类型的检查。
		 * 而引用arrayList2没有使用泛型，所以不行。
		 */

		ArrayList<String> arrayList1 = new ArrayList();
		arrayList1.add("1");// 编译通过
		// arrayList1.add(1);//编译错误
		String str1 = arrayList1.get(0);// 返回类型就是String

		ArrayList arrayList2 = new ArrayList<String>();
		arrayList2.add("1");// 编译通过
		arrayList2.add(1);// 编译通过
		Object object = arrayList2.get(0);// 返回类型就是Object

		new ArrayList<String>().add("11");// 编译通过
		// new ArrayList<String>().add(22);//编译错误
		String string = new ArrayList<String>().get(0);// 返回类型就是String
		/**
		 * 通过上面的例子，我们可以明白，类型检查就是针对引用的，谁是一个引用，用这个引用调用泛型方法，就会对这个引用调用的方法进行类型检测，
		 * 而无关它真正引用的对象。
		 */
	}

	// 这是一个简单的泛型方法
	public static <T> T add(T x, T y) {
		return y;
	}
}
