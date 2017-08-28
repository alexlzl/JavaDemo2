package com.minsheng.app.module.usercenter;

/**
 * Item definition including the section.
 */
public class MyUserEvaluationBean {
	public String orderNum;
	public String section;
	public String name;
	public String comments;

	public MyUserEvaluationBean(final String orderNum, final String section,
			String name, String comments) {
		super();
		this.orderNum = orderNum;
		this.section = section;
		this.name = name;
		this.comments = comments;
	}

	@Override
	public String toString() {
		return orderNum.toString();
	}

}
