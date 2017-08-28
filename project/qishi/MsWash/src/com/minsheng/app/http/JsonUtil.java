package com.minsheng.app.http;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.json.JSONObject;
import com.minsheng.app.util.LogUtil;
import android.text.TextUtils;

/**
 * 
 * @describe:MAP集合转换为Json字符串
 * 
 * @author:LiuZhouLiang
 * 
 * @time:2014-12-11下午2:43:01
 * 
 */
public class JsonUtil {

	public static JSONObject getJson(Map<?, ?> map) throws Exception {
		JSONObject obj = setJosn(map);
		return obj;

	}

	public static String getString(Map<?, ?> map) throws Exception {
		StringBuffer temp = new StringBuffer();
		if (!map.isEmpty()) {
			temp.append("{");
			// 遍历map
			Set<?> set = map.entrySet();
			Iterator<?> i = set.iterator();
			while (i.hasNext()) {
				Map.Entry<?, ?> entry = (Map.Entry<?, ?>) i.next();
				String key = (String) entry.getKey();
				Object value = entry.getValue();
				LogUtil.d("解析==", "key==" + key + "==========value==" + value);
				if (TextUtils.isEmpty(value + "")) {
					temp.append("\"" + key + "\":null,");
					continue;
				}
				temp.append("\"" + key + "\":");
				if (value instanceof Map<?, ?>) {
					temp.append(setJosn((Map<String, Object>) value) + ",");
				} else if (value instanceof List<?>) {
					temp.append(setList((List<Map<String, Object>>) value)
							+ ",");
				} else {
					temp.append(value + ",");

				}
			}
			if (temp.length() > 1) {
				temp = new StringBuffer(temp.substring(0, temp.length() - 1));
			}
			temp.append("}");
			LogUtil.d("解析==", "添加后" + temp);
			return temp.toString();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	private static JSONObject setJosn(Map<?, ?> map) throws Exception {
		JSONObject json = null;
		StringBuffer temp = new StringBuffer();
		if (!map.isEmpty()) {
			temp.append("{");
			// 遍历map
			Set<?> set = map.entrySet();
			Iterator<?> i = set.iterator();
			while (i.hasNext()) {
				Map.Entry<?, ?> entry = (Map.Entry<?, ?>) i.next();
				String key = (String) entry.getKey();
				Object value = entry.getValue();
				LogUtil.d("解析==", "key==" + key + "==========value==" + value);
				if (TextUtils.isEmpty(value + "")) {
					temp.append("\"" + key + "\":null,");
					continue;
				}
				temp.append("\"" + key + "\":");
				if (value instanceof Map<?, ?>) {
					temp.append(setJosn((Map<String, Object>) value) + ",");
				} else if (value instanceof List<?>) {
					temp.append(setList((List<Map<String, Object>>) value)
							+ ",");
				} else {
					temp.append(value + ",");

				}
			}
			if (temp.length() > 1) {
				temp = new StringBuffer(temp.substring(0, temp.length() - 1));
			}
			temp.append("}");
			LogUtil.d("解析==", "最终字符串" + temp);
			json = new JSONObject(temp.toString());
		}
		return json;
	}

	/**
	 * 
	 * 
	 * @describe:将单个list转成json字符串
	 * 
	 * @author:LiuZhouLiang
	 * 
	 * @time:2014-12-11下午3:01:19
	 * 
	 */
	public static String setList(List<Map<String, Object>> list)
			throws Exception {
		String jsonL = "";
		StringBuffer temp = new StringBuffer();
		temp.append("[");
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> m = list.get(i);
			if (i == list.size() - 1) {
				temp.append(setJosn(m));
			} else {
				temp.append(setJosn(m) + ",");
			}
		}
		if (temp.length() > 1) {
			temp = new StringBuffer(temp.substring(0, temp.length()));
		}
		temp.append("]");
		jsonL = temp.toString();
		return jsonL;
	}

}
