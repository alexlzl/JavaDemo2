package com.test;

import java.util.ArrayList;
import java.util.List;

/**
 * 关键字说明
 * 
 * ● ? 通配符类型
 * 
 * ● <? extends T> 表示类型的上界，表示参数化类型的可能是T 或是 T的子类
 * 
 * ● <? super T> 表示类型下界（Java Core中叫超类型限定），表示参数化类型是此类型的超类型（父类型），直至Object
 * 
 * extends 示例
 * 
 * 
 * extends 可用于的返回类型限定，不能用于参数类型限定。

super 可用于参数类型限定，不能用于返回类型限定。

带有super超类型限定的通配符可以向泛型对易用写入，带有extends子类型限定的通配符可以向泛型对象读取。——《Core Java》


 * 
 * @author lzl
 *
 */
public class 泛型extends_super<E> {
	private List<E> list;
	private List<?> list1;
	static List<? extends Fruit> flist = new ArrayList<Apple>();

	static class Food {
	}

	static class Fruit extends Food {
	}

	static class Apple extends Fruit {
	}

	static class RedApple extends Apple {
	}

	/**
	 * List<? extends Frut> 表示
	 * “具有任何从Fruit继承类型的列表”，编译器无法确定List所持有的类型，所以无法安全的向其中添加对象。可以添加null,因为null
	 * 可以表示任何类型。所以List 的add 方法不能添加任何有意义的元素，但是可以接受现有的子类型List<Apple> 赋值。
	 * 
	 * @param str
	 */
	public static void main(String[] str) {
		/**
		 * extends 示例=================1
		 */
		// complie error:
		// flist.add(new Apple());
//		 flist.add(new Fruit());
		// flist.add(new Object());
		flist.add(null); // only work for null

		/**
		 * super 示例======================2
		 */
		/**
		 * 向上转型：可以添加子类型
		 * 
		 * List<? super Fruit> 表示“具有任何Fruit超类型的列表”，列表的类型至少是一个 Fruit
		 * 类型，因此可以安全的向其中添加Fruit 及其子类型。由于List<? super Fruit>中的类型可能是任何Fruit
		 * 的超类型，无法赋值为Fruit的子类型Apple的List<Apple>.
		 */
		List<? super Fruit> flist = new ArrayList<Fruit>();

		flist.add(new Fruit());

		flist.add(new Apple());

		flist.add(new RedApple());
		/**
		 * 因为，List<? super Fruit>中的类型可能是任何Fruit 的超类型，所以编译器无法确定get返回的对象类型是Fruit,还是Fruit的父类Food 或 Object.
		 */
		// compile error:

//		Fruit item = flist.get(0);
	}
	
	public static <T> void copy(List<? super T> dest, List<? extends T> src) 
	  {
	      for (int i=0; i<src.size(); i++) 
	        dest.set(i,src.get(i)); 
	  } 
}
