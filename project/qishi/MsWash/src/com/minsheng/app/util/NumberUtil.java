package com.minsheng.app.util;

import java.math.BigDecimal;

public class NumberUtil {
	public static final int NUMBER_SCALE_TWO = 2;
	
	public static String add(String a, String b, int scale){
		BigDecimal numA= new BigDecimal(a);
		BigDecimal numB = new BigDecimal(b);
		return numA.add(numB).setScale(scale, BigDecimal.ROUND_HALF_UP).toString();
	}
	
	public static String sub(String a, String b, int scale){
		BigDecimal numA= new BigDecimal(a);
		BigDecimal numB = new BigDecimal(b);
		return numA.subtract(numB).setScale(scale, BigDecimal.ROUND_HALF_UP).toString();
	}

}
