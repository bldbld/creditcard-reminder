package com.bld.safecardreminder.bean;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

/**
 * 信用卡的一个简单描述
 * 
 * @author ballad
 * @version 1.0 beta
 */
public class CreditCard implements Serializable {

	/**
	 * Serializable 序列化
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
	public String id;

	/**
	 * 标题
	 */
	public String title;

	/**
	 * 账单日
	 */
	public int billingDay;

	/**
	 * 还款期限，在账单日之后的天数
	 */
	public int payPeriod;
	
	/**
	 * 所属银行
	 */
	public int bank;

	/**
	 * 生成ID，每次调用会重新生成一次ID
	 */
	public void generateId() {
		Random r = new Random();
		id = String.valueOf(r.nextLong() + (new Date()).getTime());
	}

	/**
	 * 返回描述信息
	 * 
	 * @return 描述信息
	 */
	public String getDesc() {
		return new StringBuilder("每月第").append(billingDay).append("天出账单，").append(payPeriod).append("天内还款").toString();
		// return "每月第" + billingDay + "天出账单，" + payPeriod + "天内还款";
	}

	public RemindInfo getRemindInfo(SettingInfo settingInfo) {
		RemindInfo rmdInfo = new RemindInfo();
		// 今天
		Calendar currDate = Calendar.getInstance();
		// System.out.println("currDate" + currDate.getTime());
		// 本月的账单日
		Calendar billingDateOfThisMonth = (Calendar) currDate.clone();
		billingDateOfThisMonth.set(Calendar.DAY_OF_MONTH, billingDay); // 账单日不会超过28
		// System.out.println("billingDateOfThisMonth"
		// +billingDateOfThisMonth.getTime());

		if (currDate.compareTo(billingDateOfThisMonth) < 0) {
			// 还未出账单
			// 上月的账单日
			Calendar billingDateOfLastMonth = (Calendar) billingDateOfThisMonth.clone();
			billingDateOfLastMonth.add(Calendar.MONTH, -1);
			rmdInfo.billingMonth = billingDateOfLastMonth.get(Calendar.MONTH);
			rmdInfo.billingYear = billingDateOfLastMonth.get(Calendar.YEAR);
			// System.out.println("billingDateOfLastMonth" +
			// billingDateOfLastMonth.getTime());
			// 上月账单的还款日
			Calendar payDateOfLastMonth = (Calendar) billingDateOfLastMonth.clone();
			payDateOfLastMonth.add(Calendar.DAY_OF_YEAR, payPeriod);
			// System.out.println("payDateOfLastMonth" +
			// payDateOfLastMonth.getTime());
			if (currDate.compareTo(payDateOfLastMonth) < 0) {
				// 未到最后还款日
				int days = payDateOfLastMonth.get(Calendar.DAY_OF_YEAR) - currDate.get(Calendar.DAY_OF_YEAR);
				if (days < settingInfo.numberOfDaysInAdvance2remind) {
					rmdInfo.remindLevel = RemindInfo.REMIND_LEVEL_REMIND_NORMAL;
				} else {
					rmdInfo.remindLevel = RemindInfo.REMIND_LEVEL_NONE;
				}
				// rmdInfo.remindText = "信用卡[" + this.title + "]还有" + days +
				// "日还款";
				rmdInfo.remindText = new StringBuilder("信用卡[").append(this.title).append("]还有").append(days).append("日还款").toString();
			} else if (currDate.compareTo(payDateOfLastMonth) == 0) {
				// 最后还款日
				rmdInfo.remindLevel = RemindInfo.REMIND_LEVEL_TODAY;
				rmdInfo.remindText = new StringBuilder("信用卡[").append(this.title).append("]最迟今日还款，请勿逾期").toString();
			} else {
				// 已过期限，但还未到下次账单日
			}
		} else {
			// 已出账单
			rmdInfo.billingMonth = billingDateOfThisMonth.get(Calendar.MONTH);
			rmdInfo.billingYear = billingDateOfThisMonth.get(Calendar.YEAR);
			// 本月账单的还款日，可能到下月
			Calendar payDate = (Calendar) billingDateOfThisMonth.clone();
			payDate.add(Calendar.DAY_OF_YEAR, payPeriod);
			// System.out.println("payDate" + payDate.getTime());
			if (currDate.compareTo(payDate) < 0) {
				// 未到最后还款日
				int days = payDate.get(Calendar.DAY_OF_YEAR) - currDate.get(Calendar.DAY_OF_YEAR);
				if (days < settingInfo.numberOfDaysInAdvance2remind) {
					rmdInfo.remindLevel = RemindInfo.REMIND_LEVEL_REMIND_NORMAL;
				} else {
					rmdInfo.remindLevel = RemindInfo.REMIND_LEVEL_NONE;
				}
				rmdInfo.remindText = new StringBuilder("信用卡[").append(this.title).append("]还有").append(days).append("日还款").toString();
			} else if (currDate.compareTo(payDate) == 0) {
				// 最后还款日
				rmdInfo.remindLevel = RemindInfo.REMIND_LEVEL_TODAY;
				rmdInfo.remindText = new StringBuilder("信用卡[").append(this.title).append("]最迟今日还款，请勿逾期").toString();
			} else {
				// 已过期限，但还未到下次账单日
			}
		}
		return rmdInfo;
	}

	// public static void main(String[] args) {
	// CreditCard cc = new CreditCard();
	// cc.title = "1";
	// cc.billingDay = 25;
	// cc.payPeriod = 25;
	// System.out.println(cc.getRemindInfo(null).remindText);
	// }
}
