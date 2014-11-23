package com.bld.safecardreminder.bean;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

/**
 * ���ÿ���һ��������
 * 
 * @author ballad
 * @version 1.0 beta
 */
public class CreditCard implements Serializable {

	/**
	 * Serializable ���л�
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
	public String id;

	/**
	 * ����
	 */
	public String title;

	/**
	 * �˵���
	 */
	public int billingDay;

	/**
	 * �������ޣ����˵���֮�������
	 */
	public int payPeriod;
	
	/**
	 * ��������
	 */
	public int bank;

	/**
	 * ����ID��ÿ�ε��û���������һ��ID
	 */
	public void generateId() {
		Random r = new Random();
		id = String.valueOf(r.nextLong() + (new Date()).getTime());
	}

	/**
	 * ����������Ϣ
	 * 
	 * @return ������Ϣ
	 */
	public String getDesc() {
		return new StringBuilder("ÿ�µ�").append(billingDay).append("����˵���").append(payPeriod).append("���ڻ���").toString();
		// return "ÿ�µ�" + billingDay + "����˵���" + payPeriod + "���ڻ���";
	}

	public RemindInfo getRemindInfo(SettingInfo settingInfo) {
		RemindInfo rmdInfo = new RemindInfo();
		// ����
		Calendar currDate = Calendar.getInstance();
		// System.out.println("currDate" + currDate.getTime());
		// ���µ��˵���
		Calendar billingDateOfThisMonth = (Calendar) currDate.clone();
		billingDateOfThisMonth.set(Calendar.DAY_OF_MONTH, billingDay); // �˵��ղ��ᳬ��28
		// System.out.println("billingDateOfThisMonth"
		// +billingDateOfThisMonth.getTime());

		if (currDate.compareTo(billingDateOfThisMonth) < 0) {
			// ��δ���˵�
			// ���µ��˵���
			Calendar billingDateOfLastMonth = (Calendar) billingDateOfThisMonth.clone();
			billingDateOfLastMonth.add(Calendar.MONTH, -1);
			rmdInfo.billingMonth = billingDateOfLastMonth.get(Calendar.MONTH);
			rmdInfo.billingYear = billingDateOfLastMonth.get(Calendar.YEAR);
			// System.out.println("billingDateOfLastMonth" +
			// billingDateOfLastMonth.getTime());
			// �����˵��Ļ�����
			Calendar payDateOfLastMonth = (Calendar) billingDateOfLastMonth.clone();
			payDateOfLastMonth.add(Calendar.DAY_OF_YEAR, payPeriod);
			// System.out.println("payDateOfLastMonth" +
			// payDateOfLastMonth.getTime());
			if (currDate.compareTo(payDateOfLastMonth) < 0) {
				// δ����󻹿���
				int days = payDateOfLastMonth.get(Calendar.DAY_OF_YEAR) - currDate.get(Calendar.DAY_OF_YEAR);
				if (days < settingInfo.numberOfDaysInAdvance2remind) {
					rmdInfo.remindLevel = RemindInfo.REMIND_LEVEL_REMIND_NORMAL;
				} else {
					rmdInfo.remindLevel = RemindInfo.REMIND_LEVEL_NONE;
				}
				// rmdInfo.remindText = "���ÿ�[" + this.title + "]����" + days +
				// "�ջ���";
				rmdInfo.remindText = new StringBuilder("���ÿ�[").append(this.title).append("]����").append(days).append("�ջ���").toString();
			} else if (currDate.compareTo(payDateOfLastMonth) == 0) {
				// ��󻹿���
				rmdInfo.remindLevel = RemindInfo.REMIND_LEVEL_TODAY;
				rmdInfo.remindText = new StringBuilder("���ÿ�[").append(this.title).append("]��ٽ��ջ����������").toString();
			} else {
				// �ѹ����ޣ�����δ���´��˵���
			}
		} else {
			// �ѳ��˵�
			rmdInfo.billingMonth = billingDateOfThisMonth.get(Calendar.MONTH);
			rmdInfo.billingYear = billingDateOfThisMonth.get(Calendar.YEAR);
			// �����˵��Ļ����գ����ܵ�����
			Calendar payDate = (Calendar) billingDateOfThisMonth.clone();
			payDate.add(Calendar.DAY_OF_YEAR, payPeriod);
			// System.out.println("payDate" + payDate.getTime());
			if (currDate.compareTo(payDate) < 0) {
				// δ����󻹿���
				int days = payDate.get(Calendar.DAY_OF_YEAR) - currDate.get(Calendar.DAY_OF_YEAR);
				if (days < settingInfo.numberOfDaysInAdvance2remind) {
					rmdInfo.remindLevel = RemindInfo.REMIND_LEVEL_REMIND_NORMAL;
				} else {
					rmdInfo.remindLevel = RemindInfo.REMIND_LEVEL_NONE;
				}
				rmdInfo.remindText = new StringBuilder("���ÿ�[").append(this.title).append("]����").append(days).append("�ջ���").toString();
			} else if (currDate.compareTo(payDate) == 0) {
				// ��󻹿���
				rmdInfo.remindLevel = RemindInfo.REMIND_LEVEL_TODAY;
				rmdInfo.remindText = new StringBuilder("���ÿ�[").append(this.title).append("]��ٽ��ջ����������").toString();
			} else {
				// �ѹ����ޣ�����δ���´��˵���
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
