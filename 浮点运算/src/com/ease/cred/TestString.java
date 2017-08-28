package com.ease.cred;

public class TestString {
	private static String s1="abcd";
	private static String s2=new String("abcd");

	public static void main(String[] args) {
		// TODO Auto-generated method stub
        System.out.println(s1.hashCode());//2987074
        System.out.println(s2.hashCode());//2987074
        System.out.println(s2==s1);//false 
        System.out.println(s2.equals(s1));//true
        
        String str1 = "str";
        String str2 = "ing";

        String str3 = "str" + "ing";
        String str4 = str1 + str2;
        System.out.println(str3 == str4);//false

        String str5 = "string";
        System.out.println(str3 == str5);//true
        
        
	}

}
