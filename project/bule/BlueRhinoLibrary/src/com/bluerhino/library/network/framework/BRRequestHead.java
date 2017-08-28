package com.bluerhino.library.network.framework;

import java.util.HashMap;

public class BRRequestHead extends HashMap<String, String> implements Cloneable {

	private static final long serialVersionUID = 6487906404477887929L;
	
	public BRRequestHead() {
		super();
	}
	
	public BRRequestHead(BRRequestHead head) {
		super();
		if (!head.isEmpty()) {
			for (String key : head.keySet()) {
				put(key, head.get(key));
			}
		}
	}

	public String put(String key, int value) {
		String intValue = String.valueOf(value);
		return super.put(key, intValue);
	}

	public String put(String key, float value) {
		String floatValue = String.valueOf(value);
		return super.put(key, floatValue);
	}

	public String put(String key, double value) {
		String doubleValue = String.valueOf(value);
		return super.put(key, doubleValue);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object clone() {
		HashMap<String, String> head = (HashMap<String, String>) super.clone();
		for (String key : keySet()) {
			head.put(key, get(key));
		}
		return head;
	}

}
