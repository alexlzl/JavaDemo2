package com.test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 执行静态块 initEnums==={0=北京市, 1=北京市, 2=云南省} initEnums==={0=北京市, 1=北京市, 2=云南省}
 * 执行getInstance 执行getCache {0=北京市, 1=北京市, 2=云南省}
 * 
 * @author lzl
 *
 */
public class CachingEnumResolver1 {
	private static final CachingEnumResolver1 SINGLE_ENUM_RESOLVER;
	public static Map CODE_MAP_CACHE;
	public static String test="testonwe";
	static {
		System.out.println("执行静态块");
		CODE_MAP_CACHE = new HashMap();
		// 为了说明问题,我在这里初始化一条数据
		CODE_MAP_CACHE.put("0", "北京市");
		SINGLE_ENUM_RESOLVER = new CachingEnumResolver1();
		initEnums();
	}

	// private, for single instance
  public  CachingEnumResolver1() {
		// 初始化加载数据 引起问题，该方法也要负点责任
		initEnums();
	}

	/**
	 * 初始化所有的枚举类型
	 */
	public static void initEnums() {
		// ~~~~~~~~~问题从这里开始暴露 ~~~~~~~~~~~//
		if (null == CODE_MAP_CACHE) {
			System.out.println("CODE_MAP_CACHE为空,问题在这里开始暴露.");
			CODE_MAP_CACHE = new HashMap();
		}
		CODE_MAP_CACHE.put("1", "北京市");
		CODE_MAP_CACHE.put("2", "云南省");
		System.out.println("initEnums===" + CODE_MAP_CACHE.toString());
		// ..... other code...
	}

	public Map getCache() {
		System.out.println("执行getCache");
		return Collections.unmodifiableMap(CODE_MAP_CACHE);
	}
    public String test(){
    	System.out.println("执行test");
    	return "test";
    }
	/**
	 * 获取单态实例
	 * 
	 * @return
	 */
	public static CachingEnumResolver1 getInstance() {
		System.out.println("执行getInstance");
		return SINGLE_ENUM_RESOLVER;
	}

	public static void main(String[] args) {
		System.out.println(CachingEnumResolver1.test);
		System.out.println(CachingEnumResolver1.getInstance().getCache());
		System.out.println(CachingEnumResolver1.getInstance().getCache());
		System.out.println(new CachingEnumResolver1().test());
	}
}
