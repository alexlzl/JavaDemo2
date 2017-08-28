package com.test;

public enum Switch语句 {
	 GREEN, YELLOW, RED;
	 public class TrafficLight {
		 Switch语句 color = Switch语句.RED;

	        public void change() {
	            switch (color) {
	            case RED:
	                color = Switch语句.GREEN;
	                break;
	            case YELLOW:
	                color = Switch语句.RED;
	                break;
	            case GREEN:
	                color = Switch语句.YELLOW;
	                break;
	            }
	        }
	    }
}
