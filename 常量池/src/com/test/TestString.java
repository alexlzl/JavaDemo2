package com.test;

/**
 * 首先说明一点，在java 中，直接使用==操作符，比较的是两个字符串的引用地址，并不是比较内容，比较内容请用String.equals()。
 * 
 * s1 ==
 * s2这个非常好理解，s1、s2在赋值时，均使用的字符串字面量，说白话点，就是直接把字符串写死，在编译期间，这种字面量会直接放入class文件的常量池中，
 * 从而实现复用，载入运行时常量池后，s1、s2指向的是同一个内存地址，所以相等。
 * 
 * s1 == s3这个地方有个坑，s3虽然是动态拼接出来的字符串，但是所有参与拼接的部分都是已知的字面量，在编译期间，这种拼接会被优化，编译器直接帮你拼好，
 * 因此String s3 = "Hel" + "lo";在class文件中被优化成String s3 = "Hello";，所以s1 == s3成立。
 * 
 * s1 == s4当然不相等，s4虽然也是拼接出来的，但new
 * String("lo")这部分不是已知字面量，是一个不可预料的部分，编译器不会优化，必须等到运行时才可以确定结果，结合字符串不变定理，
 * 鬼知道s4被分配到哪去了，所以地址肯定不同。配上一张简图理清思路：
 * 
 * @author lzl
 *
 */
public class TestString {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String[] str1 = new String[] { "a", "b" };
		String[] str2 = new String[] { "a", "b" };
		String[] str3 = { "a", "b" };
		String[] str4 = { "a", "b" };
		String s1 = "Hello";
		String s2 = "Hello";
		String s3 = "Hel" + "lo";
		String s4 = "Hel" + new String("lo");
		String s5 = new String("Hello");
		String s6 = s5.intern();
		String s7 = "H";
		String s8 = "ello";
		String s9 = s7 + s8;

		System.out.println(s1 == s2); // true
		System.out.println(s1 == s3); // true
		System.out.println(s1 == s4); // false
		System.out.println(s1 == s9); // false
		System.out.println(s4 == s5); // false
		System.out.println(s1 == s6); // true
		System.out.println(s1 == s9); // false
		System.out.println(str1.equals(str2) + "===");// false
		System.out.println(str3.equals(str4) + "===");// false
	}

}
