package com.test;

public class Person {
	String firstname, lastname;
	char sex;
	Integer age;

	public Person(String firstname, String lastname, char sex, Integer age) {

		this.firstname = firstname;
		this.lastname = lastname;
		this.sex = sex;
		this.age = age;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public char getSex() {
		return sex;
	}

	public void setSex(char sex) {
		this.sex = sex;
	}

}