package com.test;

public class Test {
	private static String tag="11.00";
	private static String tag1="2016-12-30  23:00";

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String[] str1=tag1.split(" +");
     String[] str=   tag.split("\\.");
     if(str.length==0){
    	 System.out.println("长度为0");
    	 
     }else{
    	 for(String strc:str){
    		 System.out.println(strc);
    	 }
    	 for(String strc:str1){
    		 System.out.println(strc);
    	 }
     }
        
	}

}
