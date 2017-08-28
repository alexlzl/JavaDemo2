package com.ease.cred;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class TestBigDecimal {
	 public static void main(String[] args) { 
         System.out.println("------------构造BigDecimal-------------"); 
         //从char[]数组来创建BigDecimal 
         BigDecimal bd1 = new BigDecimal("123456789.123456888".toCharArray(), 4, 12); 
         System.out.println("bd1=" + bd1); //56789.123456
         //从char[]数组来创建BigDecimal 
         BigDecimal bd2 = new BigDecimal("123456789.123456111133333213".toCharArray(), 4, 18, MathContext.DECIMAL128); 
         System.out.println("bd2=" + bd2); 
         //从字符串创建BigDecimal 
         BigDecimal bd3 = new BigDecimal("123456789.123456111133333213"); 
         System.out.println("bd3=" + bd3); 
         //从字符串创建BigDecimal，3是有效数字个数 
         BigDecimal bd4 = new BigDecimal("88.456", new MathContext(3, RoundingMode.UP)); 
         System.out.println("bd4=" + bd4); 
         System.out.println("------------使用BigDecimal-------------"); 
         System.out.println("bd1+bd2=" + bd1.add(bd2)); 
         System.out.println("bd1+bd2=" + bd1.add(bd2, new MathContext(24, RoundingMode.UP))); 
         System.out.println("bd1-bd2=" + bd1.subtract(bd2).toPlainString()); 
         System.out.println("bd1-bd2=" + bd1.subtract(bd2, new MathContext(24, RoundingMode.UP)).toPlainString()); 
         System.out.println("bd1*bd2=" + bd1.multiply(bd2)); 
         System.out.println("bd1*bd2=" + bd1.multiply(bd2, new MathContext(24, RoundingMode.UP))); 
         System.out.println("bd1/bd4=" + bd1.divideToIntegralValue(bd4)); 
         System.out.println("bd1/bd4=" + bd1.divideToIntegralValue(bd4, new MathContext(24, RoundingMode.UP))); 
         System.out.println("bd1末位数据精度=" + bd1.ulp()); 
         System.out.println("bd2末位数据精度=" + bd2.ulp()); 
         System.out.println("bd2末位数据精度=" + bd2.ulp().toPlainString()); 
         System.out.println("bd1符号：" + bd1.signum()); 
         System.out.println("bd4的标度：" + bd4.scale()); 
 } 
}
