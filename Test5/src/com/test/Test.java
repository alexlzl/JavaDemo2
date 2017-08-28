package com.test;

public class Test {
       public static void main(String[] args) {
		Father father=new Child();
		 father.father();
		 Father f=new Father();
		 if(f instanceof Child){
			 Child ch=(Child) f;
			 ch.child();
		 }else{
			 System.out.println("不能强制转换");
		 }
//		 Child child=(Child) new Father();
//		 child.child();
	}
}
