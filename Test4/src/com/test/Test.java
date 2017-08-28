package com.test;

import java.text.DecimalFormat;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		 DecimalFormat format = new DecimalFormat("0.00");
		 System.out.println(format.format(0.2344));
		  boolean swipedLeftProper = false;
		  int velocityX=-1;
		  int mFinalDelta=-1;
		    swipedLeftProper = (velocityX < 0) == (mFinalDelta < 0);
		    System.out.println(swipedLeftProper);
	}

}
