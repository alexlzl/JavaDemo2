package com.test;

public class Test{

	public static void main(String[] args) {
		// TODO Auto-generated method stub
        Class<ITest>  c=ITest.class;
        System.out.println(c.isInterface());//true
        Class<Test> t=Test.class;
        System.out.println(t.isInterface());//false
        Class<TestA> a=TestA.class;
        System.out.println(a.isAnnotation());//true
        System.out.println(a.getCanonicalName());
        System.out.println(a.getName());
        System.out.println(a.getSimpleName());
        System.out.println(a.getModifiers());
        Class<Test1> c1=Test1.class;
        System.out.println(c1.getTypeParameters());
        System.out.println(c1.getSuperclass().getName());
	}

}
