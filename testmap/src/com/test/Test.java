package com.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

public class Test {
	private static final String APP_SECURITY = "APP_SECURITY";

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Map map = new HashMap();
		map.put("page", "30");
		getBaseCommonParams(map);
	}

	public static Map<String, String> getBaseCommonParams(Map params) {
		params.put("deviceid", "dd");
		params.put("version", "2");
		params.put("terminal", "3");
		params.put("lan", "4");
		params.put("account", "16754656789");
		params.put("type", "1");
		params.put("randomnum", getRandomnum(params));

		return params;
	}

	private static String getRandomnum(Map params) {
		// //通过ArrayList构造函数把map.entrySet()转换成list
		List<Map.Entry<String, String>> list = new ArrayList<Map.Entry<String, String>>(params.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<String, String>>() {
			// 升序排序
			public int compare(Entry<String, String> o1, Entry<String, String> o2) {
				return o1.getKey().compareTo(o2.getKey());
			}

		});

		StringBuffer stringBuffer = new StringBuffer();
		for (Map.Entry<String, String> mapping : list) {
			System.out.println(mapping.getKey() + ":" + mapping.getValue());
			stringBuffer.append(mapping.getValue() + "===");
		}
		System.out.println("排序后==" + stringBuffer.toString());

		stringBuffer.append(APP_SECURITY);
		System.out.println("添加密钥后==" + stringBuffer.toString());
		String paramsf;
		paramsf = Md5Util.MD5(stringBuffer.toString());
		System.out.println("MD5后==" + paramsf);
		return paramsf;
	}

	private static void sort(Map map) {
		// 通过ArrayList构造函数把map.entrySet()转换成list
		List<Map.Entry<String, String>> list = new ArrayList<Map.Entry<String, String>>(map.entrySet());

		// 通过比较器实现比较排序
		Collections.sort(list, new Comparator<Map.Entry<String, String>>() {
			public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2) {
				return o1.getKey().compareTo(o2.getKey()); // 升序
				// return o2.getValue().compareTo (o1.getValue()); //倒序
			}
		});

		for (Map.Entry<String, String> mapping : list) {
			System.out.println(mapping.getKey() + ":" + mapping.getValue());
		}
	}

}
