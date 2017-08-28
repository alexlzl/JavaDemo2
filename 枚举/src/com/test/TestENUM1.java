package com.test;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.Map.Entry;

public class TestENUM1 {
	 public static void main(String[] args) {
	        // EnumSet的使用
	        EnumSet<EnumTest1> weekSet = EnumSet.allOf(EnumTest1.class);
	        for (EnumTest1 day : weekSet) {
	            System.out.println(day);
	        }
	 
	        // EnumMap的使用
	        EnumMap<EnumTest1, String> weekMap = new EnumMap(EnumTest1.class);
	        weekMap.put(EnumTest1.MON, "星期一");
	        weekMap.put(EnumTest1.TUE, "星期二");
	        // ... ...
	        for (Iterator<Entry<EnumTest1, String>> iter = weekMap.entrySet().iterator(); iter.hasNext();) {
	            Entry<EnumTest1, String> entry = iter.next();
	            System.out.println(entry.getKey().name() + ":" + entry.getValue());
	        }
	    }
}
