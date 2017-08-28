package com.test;


import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class param<T1, T2> {
	class A {}
	class B extends A {}
	
	private Class<T1> entityClass;
	protected param (){
		Type type = getClass().getGenericSuperclass();
		System.out.println("type = " + type);
		System.out.println("getClass()==" + getClass());
		
		Type trueType = ((ParameterizedType)type).getActualTypeArguments()[0];
		System.out.println("trueType1 = " + trueType);
		trueType = ((ParameterizedType)type).getActualTypeArguments()[1];
		System.out.println("trueType2 = " + trueType);
		this.entityClass = (Class<T1>)trueType;
		
		
		B t = new B();
		type = t.getClass().getGenericSuperclass();
	
		System.out.println("B is A's super class :" + ((ParameterizedType)type).getActualTypeArguments().length);
	}
	
}