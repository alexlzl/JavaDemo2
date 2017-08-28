package com.test;

class ParentClass {   
    public int i = 10;   
}   
  
public class SubClass extends ParentClass {   
    public int i = 30;   
  
    public static void main(String[] args) {   
        ParentClass parentClass = new SubClass();   
        SubClass subClass = new SubClass();   
        System.out.println(parentClass.i);   //10
        System.out.println( subClass.i);   //30
        System.out.println(parentClass.i + subClass.i);   //40
    }   
}  

