package com.test;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class Test extends Parent{
	public int a=10;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
      Test t=new Test();
      t.testp();
      System.out.println(t.getClass()+"==");
      System.out.println(t.getClass().getSuperclass()+"==");
      Class clazz=t.getClass();  
    //getSuperclass()获得该类的父类  
    System.out.println(clazz.getSuperclass());  
    //getGenericSuperclass()获得带有泛型的父类  
    //Type是 Java 编程语言中所有类型的公共高级接口。它们包括原始类型、参数化类型、数组类型、类型变量和基本类型。  
    Type type=clazz.getGenericSuperclass();  
    System.out.println("getGenericSuperclass=="+type);  
    //ParameterizedType参数化类型，即泛型  
    ParameterizedType p=(ParameterizedType)type;  
    //getActualTypeArguments获取参数化类型的数组，泛型可能有多个  
    Class c=(Class) p.getActualTypeArguments()[0];  
    System.out.println("getActualTypeArguments=="+c); 
	}
   private void test(){
	   System.out.println(getClass());
   }
}
