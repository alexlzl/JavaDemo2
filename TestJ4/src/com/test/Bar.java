package com.test;

public class Bar extends Foo {
	int j;

	Bar() {
		j = 2;
		System.out.println("j = 2");
	}

	@Override
	protected int getValue() {
		System.out.println("getValue");
		return j;
	}
}
