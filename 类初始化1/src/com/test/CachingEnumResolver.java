package com.test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * CODE_MAP_CACHE为空,问题在这里开始暴露. {0=北京市} 我的程序怎么会这样？为什么在 initEnum() 方法里
 * CODE_MAP_CACHE 为空？为什么我输出的 CODE_MAP_CACHE 内容只有一个元素，其它两个元素呢？？？？！！
 * 
 * 
 * 执行结果 CODE_MAP_CACHE为空,问题在这里开始暴露. initEnums==={1=北京市, 2=云南省} 执行静态块 {0=北京市}
 * 
 * @author lzl
 *
 */

public class CachingEnumResolver {
	// 单态实例 一切问题皆由此行引起
	private static final CachingEnumResolver SINGLE_ENUM_RESOLVER = new CachingEnumResolver();
	/* MSGCODE->Category内存索引 */
	private static Map CODE_MAP_CACHE;
	static {
		System.out.println("执行静态块");
		CODE_MAP_CACHE = new HashMap();
		// 为了说明问题,我在这里初始化一条数据
		CODE_MAP_CACHE.put("0", "北京市");
	}

	// private, for single instance
	private CachingEnumResolver() {
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
		return Collections.unmodifiableMap(CODE_MAP_CACHE);
	}

	/**
	 * 获取单态实例
	 * 
	 * @return
	 */
	public static CachingEnumResolver getInstance() {
		return SINGLE_ENUM_RESOLVER;
	}

	public static void main(String[] args) {
		System.out.println(CachingEnumResolver.getInstance().getCache());
	}
}
