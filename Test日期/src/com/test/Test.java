package com.test;

import java.util.Date;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		  Date d1 = new Date(2009-1900,3-1,9);

          System.out.println(d1);
          Date d3 = new Date(2009,3,9);

          System.out.println(d3);
          Date d2 = new Date();

          //年份

          int year = d2.getYear() + 1900;

          //月份

          int month = d2.getMonth() + 1;

          //日期

          int date = d2.getDate();

          //小时

          int hour = d2.getHours();

          //分钟

          int minute = d2.getMinutes();

          //秒

          int second = d2.getSeconds();

          //星期几

          int day = d2.getDay();

          System.out.println("年份：" + year);

          System.out.println("月份：" + month);

          System.out.println("日期：" + date);

          System.out.println("小时：" + hour);

          System.out.println("分钟：" + minute);

          System.out.println("秒：" + second);

          System.out.println("星期：" + day);
	}

}
