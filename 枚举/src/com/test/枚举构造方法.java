package com.test;

import java.util.EnumSet;

/**
 * 带构造器的枚举 如下：EOrderType就是枚举的构造函数 例如NormalOrder(0, "一般订单")
 * 第一个0对于构造函数的type，第二个参数对应构造函数的desc getOrderType 注意： 1、需要在枚举实例后面加上分号，然后再写构造函数等
 * 2、枚举实例必须在前面 3、定义枚举的构造器方法带参,只能为private
 * 
 * @author lzl
 *
 */

public enum 枚举构造方法 {
	/**
	 * 无效的订单类型
	 */
	Invalid(-1, "无效的订单类型"), // 需要在枚举实例后面加上分号，然后再写构造函数等

	/**
	 * 一般订单
	 */
	NormalOrder(0, "一般订单"),

	/**
	 * 虚拟礼品卡订单
	 */
	VDDmoney(50, "虚拟订单"),

	/**
	 * 实物礼品卡订单
	 */
	PDDmoney(51, "礼品卡订单");
	private int orderType;
	private String description;

	/**
	 * 定义枚举的构造器方法带参,只能为private
	 * 
	 * @param type
	 * @param desc
	 */
	private 枚举构造方法(int type, String desc) {
		this.orderType = type;
		this.description = desc;
	}
   /*
    * enum 类型不支持 public 和 protected 修饰符的构造方法，因此构造函数一定要是 private 或 friendly 的。也正因为如此，所以枚举对象是无法在程序中通过直接调用其构造方法来初始化的。
    */
	private 枚举构造方法() {

	}

	public int getOrderType() {
		return this.orderType;
	}

	public String getDescription() {
		return this.description;
	}

	public static EnumSet<枚举构造方法> getSetTypes() {
		EnumSet<枚举构造方法> set = EnumSet.noneOf(枚举构造方法.class);
		set.addAll(EnumSet.complementOf(set));
		return set;
	}

	public static void main(String[] str) {
		枚举构造方法.NormalOrder.getOrderType();
		枚举构造方法.VDDmoney.getDescription();
	}

}
