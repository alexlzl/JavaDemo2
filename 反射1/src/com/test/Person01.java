package com.test;

public class Person01 {
	private   Person01() {
	}
	private Person01(String name){}
	public  Person01(int age){}
	private Person01(String name, int age) {
		this.name = name;
		this.age = age;
	}

	private String name;
	private int age;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Person{" + "name='" + name + '\'' + ", age=" + age + '}';
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public void sayChina() {
		System.out.println("hello China");
	}

	public void sayHello(String name, int age) {
		System.out.println(name + "   " + age);
	}
}
