package com.test;

import java.util.ArrayList;
import java.util.List;

public class Foo<T>  {
	private T str;
//	public Foo(String str){
//		this.str=str;
//	}
	List<String>[] ls = new ArrayList[10]; 
	public T get(){
		return str;
		
	}
	public int getInt(){
		return 0;
	}

}
