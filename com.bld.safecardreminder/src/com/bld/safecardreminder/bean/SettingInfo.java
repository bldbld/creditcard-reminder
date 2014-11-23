package com.bld.safecardreminder.bean;

/**
 * 配置信息
 * 
 * @author ballad
 * @version 1.0 beta
 */
public class SettingInfo {

	/**
	 * 提前提醒天数
	 */
	public int numberOfDaysInAdvance2remind = 5;

	/**
	 * 将配置信息转换为字符串
	 */
	public String toString() {
		return String.valueOf(this.numberOfDaysInAdvance2remind);
	}

	/**
	 * 通过字符串解析配置信息
	 * 
	 * @param data
	 */
	public void initSettingInfo(String data) {
		this.numberOfDaysInAdvance2remind = Integer.parseInt(data);
	}
}
