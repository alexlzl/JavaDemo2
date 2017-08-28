package com.ease.cred;

/**
 * 包装对象==相应值”
 * 这样的比较是可行的，但却不是推荐的。因为解析String实例为Integer实例,然后在去Integer实例里面去取的int值进行比较，
 * 在此Integer实例就多此一举了，还不如直接使用parseInt解析字符为相应的int值直接进行比较，节省创建一个Integer实例所浪费的资源。
 * 
 * @author lzl
 *
 */
public class TestIntegerCompare {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Object obj = new Integer(1024);
		if (Integer.valueOf(obj.toString()) == 1024) {
			System.out.println("bing go! Integer.valueOf(obj.toString())==1024 is true");
		}
	}

}
