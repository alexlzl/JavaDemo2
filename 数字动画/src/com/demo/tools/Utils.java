package com.demo.tools;

import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * �����ļ�
 * @author zengtao 2015��7��17�� ����11:47:44 
 *
 */
public class Utils {
	/**
	 * ��ʽ��
	 */
	private static DecimalFormat dfs = null;

	public static DecimalFormat format(String pattern) {
		if (dfs == null) {
			dfs = new DecimalFormat();
		}
		dfs.setRoundingMode(RoundingMode.FLOOR);
		dfs.applyPattern(pattern);
		return dfs;
	}
}
