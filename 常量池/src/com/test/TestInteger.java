package com.test;

/**
 * java中基本类型的包装类的大部分都实现了常量池技术，
即Byte,Short,Integer,Long,Character,Boolean；
 * 从这个静态块可以看出，Integer已经默认创建了数值【-128-127】的Integer缓存数据。所以使用Integer
 * i1=40时，JVM会直接在该在对象池找到该值的引用。
 * 
 * 
 * 也就是说这种方式声明一个Integer对象时，JVM首先会在Integer对象的缓存池中查找有木有值为40的对象，如果有直接返回该对象的引用；如果没有，
 * 则使用New keyword创建一个对象，并返回该对象的引用地址。因为Java中【==】比较的是两个对象是否是同一个引用（即比较内存地址），
 * i2和i2都是引用的同一个对象，So i1==i2结果为”true“；而使用new方式创建的i4=new Integer(40)、i5=new
 * Integer(40)，虽然他们的值相等，但是每次都会重新Create新的Integer对象，不会被放入到对象池中，所以他们不是同一个引用，
 * 输出false。
 * 
 * 对于i1==i2+i3、i4==i5+i6结果为True，是因为，Java的数学计算是在内存栈里操作的，Java会对i5、i6进行拆箱操作，
 * 其实比较的是基本类型（40=40+0），他们的值相同，因此结果为True。
 * 
 * @author lzl
 *
 */
public class TestInteger {
	public static void main(String[] args) {
		objPoolTest();
	}

	public static void objPoolTest() {
		Integer i1 = 40;
		Integer i2 = 40;
		Integer i3 = 0;
		Integer i4 = new Integer(40);
		Integer i5 = new Integer(40);
		Integer i6 = new Integer(0);
		Integer i7=400;
		Integer i8=400;
		Double d1=1.2;
		Double d2=1.2;
		System.out.println(d1==d2);//输出false
		System.out.println("i1=i2\t" + (i1 == i2));// true
		System.out.println("i1=i2+i3\t" + (i1 == i2 + i3));// true
		System.out.println("i4=i5\t" + (i4 == i5));// false
		System.out.println("i4=i5+i6\t" + (i4 == i5 + i6)); // true
		System.out.println(i1+i3==i2+i3);//true
		System.out.println(i7==i8);//false
	}

}
