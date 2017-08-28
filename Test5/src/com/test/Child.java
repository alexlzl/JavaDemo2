package com.test;

public class Child extends Father {
   public void child(){
	   System.out.println("child");
   }
   @Override
	public void father() {
		// TODO Auto-generated method stub
		super.father();
	   System.out.println("子类重写father");
	}
   public Child(){
//	   super(0);
	   this.father();
   }
}
