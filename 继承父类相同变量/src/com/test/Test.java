package com.test;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Father a = new Father();
		Chilren b = new Chilren();
		Father c = new Chilren();
		a.getAge();//40
		System.out.println(a.age);//40
		b.getAge();//18
		System.out.println(b.age);//18
		c.getAge();//18
		System.out.println(c.age);//40
	}

}

class Father {
	int age = 40;

	public void getAge() {
		System.out.println(age);
	}
}

class Chilren extends Father {
	int age = 18;

	public void getAge() {
		System.out.println(age);
	}
}