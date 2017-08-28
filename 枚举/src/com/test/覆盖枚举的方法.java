package com.test;

public enum 覆盖枚举的方法 {
	  RED("红色", 1), GREEN("绿色", 2), BLANK("白色", 3), YELLO("黄色", 4);
    // 成员变量
    private String name;
    private int index;

    // 构造方法
    private 覆盖枚举的方法(String name, int index) {
        this.name = name;
        this.index = index;
    }

    // 覆盖方法
    @Override
    public String toString() {
        return this.index + "_" + this.name;
    }

	    public static void main(String[] args) {
	        System.out.println(覆盖枚举的方法.RED.toString());
	    }
}
