package com.test;

import java.util.Arrays;

/*
 * 谁的姓拼音靠前，谁就排前面。
 * 然后对名字进行排序。如果同名，女性排前头。
 * 如果名字和性别都相同，年龄小的排前头。
 *


 */
public class Main {
	public Main() {
	}

	public static void main(String[] args) {
		Person[] person = new Person[] { new Person("ouyang", "feng", '男', new Integer(27)),
				new Person("zhuang", "gw", '男', new Integer(27)), new Person("deng", "jx", '男', new Integer(28)),
				new Person("deng", "jx", '女', new Integer(27)),

		};

		for (int i = 0; i < person.length; i++) {
			System.out.println("排序前=" + person[i].getFirstname() + "  " + person[i].getLastname() + "  "
					+ person[i].getAge() + "  " + person[i].getSex());
		}
		/*
		 * 根据sort方法的第一个参数是person，然后在Comparators.getComparator()方法中找到对应的方法
		 * compare(Person o1, Person o2)，参数与person类型一致
		 */
		java.util.Arrays.sort(person, Comparators.getComparator());

		System.out.println("————————————————————————————————————————————————————————————————————————");
		for (int i = 0; i < person.length; i++) {
			System.out.println("排序后=" + person[i].getFirstname() + "  " + person[i].getLastname() + "  "
					+ person[i].getAge() + "  " + person[i].getSex());
		}
		System.out.println("————————————————————————————————————————————————————————————————————————");

		Integer[] data = new Integer[] { 2, 3, 1 };

		for (int i = 0; i < data.length; i++) {
			System.out.println("排序前=" + data[i] + "  ");
		}
		Arrays.sort(data, Comparators.getComparator());
		System.out.println("————————————————————————————————————————————————————————————————————————");

		for (int i = 0; i < data.length; i++) {
			System.out.println("排序后=" + data[i] + "  ");
		}

	}

}
