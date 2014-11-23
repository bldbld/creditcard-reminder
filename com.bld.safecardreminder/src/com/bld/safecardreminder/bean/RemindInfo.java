package com.bld.safecardreminder.bean;

/**
 * 用于反馈给用户的提示信息
 * 
 * @author ballad
 * @version 1.0 beta
 */
public class RemindInfo {

	/**
	 * 提醒级别-无
	 */
	public static final int REMIND_LEVEL_NONE = 0;
	
	/**
	 * 提醒级别-一级-已经超期
	 */
	public static final int REMIND_LEVEL_OVERTIME = 1;
	
	/**
	 * 提醒级别-在提醒器内
	 */
	public static final int REMIND_LEVEL_REMIND_NORMAL = 2;
	
	/**
	 * 提醒级别-今日需还款
	 */
	public static final int REMIND_LEVEL_TODAY= 3;

	/**
	 * 提醒级别
	 */
	public int remindLevel = REMIND_LEVEL_NONE;

	/**
	 * 提醒内容
	 */
	public String remindText = null;

	/**
	 * 默认提醒标题
	 */
	public static final String REMIND_TITLE = "信用卡还款提醒";

	/**
	 * 账单年
	 */
	public int billingYear = -1;

	/**
	 * 账单月
	 */
	public int billingMonth = -1;
}
