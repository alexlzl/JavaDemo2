package com.test;

public class UseSeason {
	/**
	 * 将英文的季节转换成中文季节
	 * 
	 * @param season
	 * @return
	 */
	public String getChineseSeason(Season season) {
		StringBuffer result = new StringBuffer();
		switch (season) {
		case SPRING:
			result.append("[中文：春天，枚举常量:" + season.name() + "，数据:" + season.getCode() + "]");
			break;
		case AUTUMN:
			result.append("[中文：秋天，枚举常量:" + season.name() + "，数据:" + season.getCode() + "]");
			break;
		case SUMMER:
			result.append("[中文：夏天，枚举常量:" + season.name() + "，数据:" + season.getCode() + "]");
			break;
		case WINTER:
			result.append("[中文：冬天，枚举常量:" + season.name() + "，数据:" + season.getCode() + "]");
			break;
		default:
			result.append("地球没有的季节 " + season.name());
			break;
		}
		return result.toString();
	}

	public void doSomething() {
		for (Season s : Season.values()) {
			System.out.println(getChineseSeason(s));// 这是正常的场景
		}
		// System.out.println(getChineseSeason(5));
		// 此处已经是编译不通过了，这就保证了类型安全
	}

	/**
	 * [中文：春天，枚举常量:SPRING，数据:1] [中文：夏天，枚举常量:SUMMER，数据:2]
	 * [中文：秋天，枚举常量:AUTUMN，数据:3] [中文：冬天，枚举常量:WINTER，数据:4]
	 * 
	 * @param arg
	 */
	public static void main(String[] arg) {
		UseSeason useSeason = new UseSeason();
		useSeason.doSomething();
		short i=1;
		switch(i){
		case 0:
			break;
		case 1:
			break;
		}
		char[] c=new char[]{'a','b','c'};
		switch(c[0]){
		case 'a':
			System.out.println("a===");
			break;
		case 'b':
			break;
				
		}
	}

	public enum Season {
		SPRING(10), SUMMER(20), AUTUMN(30), WINTER(40);

		private int code;

		private Season(int code) {
			this.code = code;
		}

		public int getCode() {
			return code;
		}
	}
}
