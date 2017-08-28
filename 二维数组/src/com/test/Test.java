package com.test;

public class Test {

	public static void main(String[] args) {  
	    int[][] arr = new int [3][3];//定义一个二维数组  
	    int sum =0;//记录长度  
	    for(int a = 0 ;a<arr.length;a++){//获取行的长度  
	    	 System.out.println("第"+a+"行");
	        for(int b = 0 ;b<arr[a].length;b++){//获取列的长度  
	       	 System.out.println("第"+a+"行"+"第"+b+"列");
	            sum++;//长度+1  
	           
	        }  
	    }  
	    System.out.println(sum);//输出长度  
	  
	}  

}
