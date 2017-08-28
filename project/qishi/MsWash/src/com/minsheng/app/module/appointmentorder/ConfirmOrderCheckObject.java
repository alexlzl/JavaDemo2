package com.minsheng.app.module.appointmentorder;

import java.io.Serializable;

/**
 * 
 * @describe:存储对应衣服备注选择信息
 * 
 * @author:LiuZhouLiang
 * 
 * @time:2015-3-13上午11:10:41
 * 
 */
public class ConfirmOrderCheckObject implements Serializable{
	private static final long serialVersionUID = 1L;
	public String checkOne;
	public String checkTwo;
	public String checkThree;
	public String checkFour;
	public String checkFive;
	public String checkSix;
	public boolean isChcekStateOne;
	public boolean isChcekStateTwo;
	public boolean isChcekStateThree;
	public boolean isChcekStateFour;
	public boolean isChcekStateFive;

	public boolean isChcekStateOne() {
		return isChcekStateOne;
	}

	public void setChcekStateOne(boolean isChcekStateOne) {
		this.isChcekStateOne = isChcekStateOne;
	}

	public boolean isChcekStateTwo() {
		return isChcekStateTwo;
	}

	public void setChcekStateTwo(boolean isChcekStateTwo) {
		this.isChcekStateTwo = isChcekStateTwo;
	}

	public boolean isChcekStateThree() {
		return isChcekStateThree;
	}

	public void setChcekStateThree(boolean isChcekStateThree) {
		this.isChcekStateThree = isChcekStateThree;
	}

	public boolean isChcekStateFour() {
		return isChcekStateFour;
	}

	public void setChcekStateFour(boolean isChcekStateFour) {
		this.isChcekStateFour = isChcekStateFour;
	}

	public boolean isChcekStateFive() {
		return isChcekStateFive;
	}

	public void setChcekStateFive(boolean isChcekStateFive) {
		this.isChcekStateFive = isChcekStateFive;
	}

	public String getCheckOne() {
		return checkOne;
	}

	public void setCheckOne(String checkOne) {
		this.checkOne = checkOne;
	}

	public String getCheckTwo() {
		return checkTwo;
	}

	public void setCheckTwo(String checkTwo) {
		this.checkTwo = checkTwo;
	}

	public String getCheckThree() {
		return checkThree;
	}

	public void setCheckThree(String checkThree) {
		this.checkThree = checkThree;
	}

	public String getCheckFour() {
		return checkFour;
	}

	public void setCheckFour(String checkFour) {
		this.checkFour = checkFour;
	}

	public String getCheckFive() {
		return checkFive;
	}

	public void setCheckFive(String checkFive) {
		this.checkFive = checkFive;
	}

	public String getCheckSix() {
		return checkSix;
	}

	public void setCheckSix(String checkSix) {
		this.checkSix = checkSix;
	}

	@Override
	public String toString() {
		return "ConfirmOrderCheckObject [checkOne=" + checkOne + ", checkTwo="
				+ checkTwo + ", checkThree=" + checkThree + ", checkFour="
				+ checkFour + ", checkFive=" + checkFive + ", checkSix="
				+ checkSix + ", isChcekStateOne=" + isChcekStateOne
				+ ", isChcekStateTwo=" + isChcekStateTwo
				+ ", isChcekStateThree=" + isChcekStateThree
				+ ", isChcekStateFour=" + isChcekStateFour
				+ ", isChcekStateFive=" + isChcekStateFive + "]";
	}

	

}
