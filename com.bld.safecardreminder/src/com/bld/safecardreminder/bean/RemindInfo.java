package com.bld.safecardreminder.bean;

/**
 * ���ڷ������û�����ʾ��Ϣ
 * 
 * @author ballad
 * @version 1.0 beta
 */
public class RemindInfo {

	/**
	 * ���Ѽ���-��
	 */
	public static final int REMIND_LEVEL_NONE = 0;
	
	/**
	 * ���Ѽ���-һ��-�Ѿ�����
	 */
	public static final int REMIND_LEVEL_OVERTIME = 1;
	
	/**
	 * ���Ѽ���-����������
	 */
	public static final int REMIND_LEVEL_REMIND_NORMAL = 2;
	
	/**
	 * ���Ѽ���-�����軹��
	 */
	public static final int REMIND_LEVEL_TODAY= 3;

	/**
	 * ���Ѽ���
	 */
	public int remindLevel = REMIND_LEVEL_NONE;

	/**
	 * ��������
	 */
	public String remindText = null;

	/**
	 * Ĭ�����ѱ���
	 */
	public static final String REMIND_TITLE = "���ÿ���������";

	/**
	 * �˵���
	 */
	public int billingYear = -1;

	/**
	 * �˵���
	 */
	public int billingMonth = -1;
}
